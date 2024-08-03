package com.ichsanalfian.elog_pdam.ui.main.buyer.ui.keranjang

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ichsanalfian.elog_pdam.R
import com.ichsanalfian.elog_pdam.databinding.FragmentKeranjangBinding
import com.ichsanalfian.elog_pdam.local.UserPreferences
import com.ichsanalfian.elog_pdam.ui.main.buyer.BuyerActivity
import com.ichsanalfian.elog_pdam.adapter.LinearAdapter
import com.ichsanalfian.elog_pdam.model.Barang
import com.ichsanalfian.elog_pdam.viewModel.BuyerViewModel
import com.ichsanalfian.elog_pdam.viewModel.ViewModelFactory
import java.io.File
import java.text.NumberFormat
import java.util.*


class KeranjangFragment : Fragment() {
    private var _binding: FragmentKeranjangBinding? = null
    private val binding get() = _binding!!
    private lateinit var keranjangViewModel: BuyerViewModel
    private lateinit var keranjangAdapter: LinearAdapter

    //pdf print
    var pageHeight = 1120
    var pageWidth = 792
    var PERMISSION_CODE = 101

    lateinit var bmp: Bitmap
    lateinit var scaledbmp: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKeranjangBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keranjangAdapter = LinearAdapter(0)

        bmp = BitmapFactory.decodeResource(resources, R.drawable.logoatasan_new)
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false)

        val layout = LinearLayoutManager(requireContext())
        binding.rvKeranjang.apply {
            layoutManager = layout
            adapter = keranjangAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(requireContext(), layout.orientation))
        }

        keranjangViewModel = ViewModelProvider(this, ViewModelFactory())[BuyerViewModel::class.java]
        keranjangViewModel.getKeranjang()
        keranjangViewModel.getBarang().observe(viewLifecycleOwner) { keranjangList ->
            if (keranjangList != null) {
                keranjangAdapter.submitList(keranjangList)
            } else {
                println("KOSONG")
                binding.emptyData3.visibility = View.VISIBLE
                binding.btnCheckout.isEnabled = false
            }
        }

        keranjangViewModel.isLoad().observe(viewLifecycleOwner){
            showLoading(it)
        }

        //pdf
        if (checkPermissions()) {
            Toast.makeText(requireContext(), "Permissions Granted..", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }

        binding.btnCheckout.setOnClickListener {
            val userId = UserPreferences.user.id.toString()
            keranjangViewModel.moveToHistory(userId)

            Toast.makeText(requireContext(), "Produk anda telah terCheckout", Toast.LENGTH_SHORT)
                .show()
            keranjangViewModel.moveToHistoryResult.observe(this) {
                if (it.first) {
                    startActivity(Intent(requireContext(), BuyerActivity::class.java))
                }
            }
            val currentDate = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())

            keranjangViewModel.getBarang().observe(this) { keranjangList ->
                if (keranjangList != null) {
                    generatePDF(keranjangList, currentDate)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showLoading(state: Boolean){
//        if (state) View.VISIBLE else View.GONE
        if (state){
            binding.progressBar2.visibility = View.VISIBLE
        } else{
            binding.progressBar2.visibility = View.GONE
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun generatePDF(keranjangList: List<Barang>, tanggalCheckout: String) {
        var pdfDocument: PdfDocument = PdfDocument()
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("id"))
        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        var paint: Paint = Paint()
        var title: Paint = Paint()

        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        var myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        // below line is used for setting
        // start page for our PDF file.
        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        var canvas: Canvas = myPage.canvas

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 56F, 40F, paint)

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))

        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.textSize = 20F

        // below line is sued for setting color
        // of our text inside our PDF file.
        title.setColor(ContextCompat.getColor(requireContext(), R.color.purple_200))

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawText("INVOICE PDAM SURAKARTA", 209F, 80F, title)
        canvas.drawText("Tanggal Checkout: $tanggalCheckout", 209F, 105F, title)
        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        title.setColor(ContextCompat.getColor(requireContext(), R.color.black))
        title.textSize = 15F

        // below line is used for setting
        // our text to center of PDF.
        title.textAlign = Paint.Align.CENTER
        val startY = 300F

        // Draw table header
        val startX = 50F

        var currentX = startX
        val headerHeight = 40F
        val cellWidth = 115F
        // Draw table header cells with border lines
        val headerPaint = Paint()
        headerPaint.style = Paint.Style.STROKE
        headerPaint.strokeWidth = 2F

        canvas.drawRect(startX, startY, startX + cellWidth, startY + headerHeight, headerPaint)
        canvas.drawText("No", currentX + cellWidth / 2, startY + headerHeight / 2, title)

        currentX += cellWidth
        canvas.drawRect(currentX, startY, currentX + cellWidth, startY + headerHeight, headerPaint)
        canvas.drawText("Nama", currentX + cellWidth / 2, startY + headerHeight / 2, title)

        currentX += cellWidth
        canvas.drawRect(currentX, startY, currentX + cellWidth, startY + headerHeight, headerPaint)
        canvas.drawText("Kode", currentX + cellWidth / 2, startY + headerHeight / 2, title)

        currentX += cellWidth
        canvas.drawRect(currentX, startY, currentX + cellWidth, startY + headerHeight, headerPaint)
        canvas.drawText("Perusahaan", currentX + cellWidth / 2, startY + headerHeight / 2, title)

        currentX += cellWidth
        canvas.drawRect(currentX, startY, currentX + cellWidth, startY + headerHeight, headerPaint)
        canvas.drawText("Jumlah", currentX + cellWidth / 2, startY + headerHeight / 2, title)

        currentX += cellWidth
        canvas.drawRect(currentX, startY, currentX + cellWidth, startY + headerHeight, headerPaint)
        canvas.drawText("Harga", currentX + cellWidth / 2, startY + headerHeight / 2, title)

// Draw table rows
        val rowHeight = 30F

        var currentY = startY + headerHeight
        var no = 1 // Initialize the number

        for (item in keranjangList) {
            currentX = startX // Reset X for each row

            // Draw table row cells with border lines
            canvas.drawRect(startX, currentY, startX + cellWidth, currentY + rowHeight, headerPaint)
            canvas.drawText(no.toString(), currentX + cellWidth / 2, currentY + rowHeight / 2, title)

            currentX += cellWidth
            canvas.drawRect(currentX, currentY, currentX + cellWidth, currentY + rowHeight, headerPaint)
            canvas.drawText(item.nama, currentX + cellWidth / 2, currentY + rowHeight / 2, title)

            currentX += cellWidth
            canvas.drawRect(currentX, currentY, currentX + cellWidth, currentY + rowHeight, headerPaint)
            canvas.drawText(item.kode, currentX + cellWidth / 2, currentY + rowHeight / 2, title)

            currentX += cellWidth
            canvas.drawRect(currentX, currentY, currentX + cellWidth, currentY + rowHeight, headerPaint)
            canvas.drawText(item.perusahaan.toString(), currentX + cellWidth / 2, currentY + rowHeight / 2, title)

            currentX += cellWidth
            canvas.drawRect(currentX, currentY, currentX + cellWidth, currentY + rowHeight, headerPaint)
            canvas.drawText(item.jumlah.toString(), currentX + cellWidth / 2, currentY + rowHeight / 2, title)

            currentX += cellWidth
            canvas.drawRect(currentX, currentY, currentX + cellWidth, currentY + rowHeight, headerPaint)
            canvas.drawText(currencyFormat.format(item.harga).substring(1,10), currentX + cellWidth / 2, currentY + rowHeight / 2, title)

            currentY += rowHeight
            no++ // Increment the number for the next row
        }

// Draw table border lines
        canvas.drawRect(startX, startY, startX + cellWidth * 4, currentY, headerPaint)
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
        title.textSize = 20F
        // Add total harga to PDF
        // Format sebagai mata uang dengan tanda titik dan koma sesuai konvensi Indonesia
        val totalHarga = keranjangList.sumBy { it.jumlah!! * it.harga }
        val totalHargaInfo = "Total Harga: Rp" + currencyFormat.format(totalHarga).substring(1,10)
        canvas.drawText(totalHargaInfo, 580F, currentY + 30F, title)


        // after adding all attributes to our
        // PDF file we will be finishing our page.
        pdfDocument.finishPage(myPage)

        val downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDirectory, "${tanggalCheckout}_Invoice_PDAM.pdf")

        try {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "${tanggalCheckout}_Invoice_PDAM.pdf")
                put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val resolver = requireContext().contentResolver
            val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            } else {
                TODO("VERSION.SDK_INT < Q")
            }

            val outputStream = uri?.let { resolver.openOutputStream(it) }
            pdfDocument.writeTo(outputStream)
            outputStream?.close()
            pdfDocument.close()

            Toast.makeText(requireContext(), "PDF file generated and saved in Downloads directory.", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Failed to generate and save PDF file.", Toast.LENGTH_SHORT).show()
        }
    }


    fun checkPermissions(): Boolean {
        // on below line we are creating a variable for both of our permissions.

        // on below line we are creating a variable for
        // writing to external storage permission
        var writeStoragePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        // on below line we are creating a variable
        // for reading external storage permission
        var readStoragePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Periksa izin menggunakan Environment.isExternalStorageManager().
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Environment.isExternalStorageManager()
            } else {
                TODO("VERSION.SDK_INT < R")
            }
        }

        // on below line we are returning true if both the
        // permissions are granted and returning false
        // if permissions are not granted.
        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    // on below line we are creating a function to request permission.
    fun requestPermission() {

        // on below line we are requesting read and write to
        // storage permission for our application.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            // Request MANAGE_EXTERNAL_STORAGE permission
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:${requireContext().packageName}")
            startActivityForResult(intent, PERMISSION_CODE)
        }else{
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_CODE
            )
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        // on below line we are checking if the
        // request code is equal to permission code.
        if (requestCode == PERMISSION_CODE) {

            // on below line we are checking if result size is > 0
            if (grantResults.size > 0) {

                // on below line we are checking
                // if both the permissions are granted.
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED) {

                    // if permissions are granted we are displaying a toast message.
                    Toast.makeText(requireContext(), "Permission Granted..", Toast.LENGTH_SHORT).show()

                } else {

                    // if permissions are not granted we are
                    // displaying a toast message as permission denied.
                    Toast.makeText(requireContext(), "Permission Denied..", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

}