package com.ichsanalfian.elog_pdam.ui.main.seller.verif

import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.ichsanalfian.elog_pdam.R
import com.ichsanalfian.elog_pdam.databinding.FragmentProdukBinding
import com.ichsanalfian.elog_pdam.databinding.FragmentVerifyBinding
import com.ichsanalfian.elog_pdam.local.UserPreferences
import com.ichsanalfian.elog_pdam.model.VerifSellerData
import com.ichsanalfian.elog_pdam.ui.main.seller.SellerActivity
import com.ichsanalfian.elog_pdam.ui.main.seller.add.TambahBarangActivity
import com.ichsanalfian.elog_pdam.viewModel.AdminViewModel
import com.ichsanalfian.elog_pdam.viewModel.SellerViewModel
import com.ichsanalfian.elog_pdam.viewModel.ViewModelFactory
import okhttp3.internal.wait
import java.io.*

class VerifyFragment : Fragment(){
    private lateinit var binding: FragmentVerifyBinding
    private lateinit var selectedImageFileLogo: File
    private lateinit var selectedImageFileKTP: File
    private var selectedImage: Bitmap? = null

    private lateinit var sellerViewModel: SellerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVerifyBinding.inflate(inflater, container, false)
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sellerViewModel = ViewModelProvider(this, ViewModelFactory())[SellerViewModel::class.java]
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Jika izin belum diberikan, minta izin kamera secara dinamis
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Jika izin sudah diberikan, Anda dapat membuka kamera
        }

        binding.buttonUploadLogo.setOnClickListener {
            showImagePickerDialog(0)
        }

        binding.buttonUploadKTP.setOnClickListener {
            showImagePickerDialog(1)
        }

        binding.buttonVerif.setOnClickListener {
            binding.apply {
                val namaPerusahaan = editTextNamaPerusahaan.text.toString()
                val alamat = editTextAlamat.text.toString()
                val namaDir = editTextNamaDir.text.toString()
                val noTelp = editTextTelp.text.toString()
                val noWA = editTextWA.text.toString()
                val email = editTextEmail.text.toString()
                val npwp = editTextNPWP.text.toString()
                val bidangUsaha = editTextBidangUsaha.text.toString()
                val nib = editTextNIB.text.toString()
                val kbli = editTextKBLI.text.toString()

                if (namaPerusahaan.isNotEmpty() && alamat.isNotEmpty() && namaDir.isNotEmpty() && noTelp.isNotEmpty()
                    && noWA.isNotEmpty() && email.isNotEmpty() && npwp.isNotEmpty() && bidangUsaha.isNotEmpty()
                    && nib.isNotEmpty() && kbli.isNotEmpty() && selectedImageFileLogo != null && selectedImageFileKTP != null)
                {
                    val verifSellerData = VerifSellerData(
                        null,
                        namaPerusahaan,
                        alamat,
                        selectedImageFileLogo.name,
                        namaDir,
                        selectedImageFileKTP.name,
                        noTelp,
                        noWA,
                        email,
                        npwp,
                        bidangUsaha,
                        nib,
                        kbli,
                        null,
                    )
                    sellerViewModel.postVerif(verifSellerData,selectedImageFileLogo, selectedImageFileKTP)
                    showSuccessDialog()

                }else{
                    Toast.makeText(requireContext(), "Mohon isi Semua isi kolom", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Jika izin kamera diberikan, buka kamera

            } else {
                Toast.makeText(requireContext(), "Izin kamera dibutuhkan untuk mengakses kamera", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun saveImageToFile(bitmap: Bitmap?, req : Int): File {
        // Create a file in the cache directory to store the image
        var imgName =""
        if (req == REQUEST_IMAGE_CAPTURE_LOGO || req == REQUEST_IMAGE_PICK_LOGO){
            imgName = "LOGO__${binding.editTextNamaPerusahaan.text}.jpg"
        } else{
            imgName = "KTP__${binding.editTextNamaPerusahaan.text}.jpg"
        }
        val file = File(requireActivity().cacheDir, imgName)
        try {
            val stream = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, stream)
            stream.flush()
            stream.close()
            return file
        } catch (e: IOException) {
            e.printStackTrace()
        }
        throw FileNotFoundException("Failed to save image to file.")
    }


    private fun showImagePickerDialog(imageType: Int) {
        val items = arrayOf<CharSequence>("Kamera", "Galeri")
        val builder = android.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Pilih Sumber Foto")
        builder.setItems(items) { _, item ->
            when (item) {
                0 -> takePictureFromCamera(imageType)
                1 -> pickImageFromGallery(imageType)
            }
        }
        builder.show()
    }

    private fun takePictureFromCamera(imageType: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (imageType == 0){
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_LOGO)
        } else{
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_KTP)
        }
    }

    private fun pickImageFromGallery(imageType: Int) {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (imageType == 0){
            startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK_LOGO)
        } else{
            startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK_KTP)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE_LOGO -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    selectedImage = imageBitmap

                    binding.imageViewLogo.setImageBitmap(selectedImage)
                    selectedImageFileLogo = saveImageToFile(selectedImage, requestCode)
                }
                REQUEST_IMAGE_PICK_LOGO -> {
                    val imageUri: Uri? = data?.data
                    if (imageUri != null) {
                        try {
                            val imageStream: InputStream? = requireContext().contentResolver.openInputStream(imageUri)
                            selectedImage = BitmapFactory.decodeStream(imageStream)

                            binding.imageViewLogo.setImageBitmap(selectedImage)
                            selectedImageFileLogo = saveImageToFile(selectedImage, requestCode)

                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                            Toast.makeText(requireContext(), "Gambar tidak ditemukan!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                REQUEST_IMAGE_CAPTURE_KTP -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    selectedImage = imageBitmap

                    binding.imageViewKTP.setImageBitmap(selectedImage)
                    selectedImageFileKTP = saveImageToFile(selectedImage, requestCode)
                }
                REQUEST_IMAGE_PICK_KTP -> {
                    val imageUri: Uri? = data?.data
                    if (imageUri != null) {
                        try {
                            val imageStream: InputStream? = requireContext().contentResolver.openInputStream(imageUri)
                            selectedImage = BitmapFactory.decodeStream(imageStream)

                            binding.imageViewKTP.setImageBitmap(selectedImage)
                            selectedImageFileKTP = saveImageToFile(selectedImage, requestCode)

                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                            Toast.makeText(requireContext(), "Gambar tidak ditemukan!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun showSuccessDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setTitle("YEAY")
        dialogBuilder.setMessage("Data Verifikasi berhasil diunggah!")
        dialogBuilder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, _ ->
            // Tindakan setelah tombol OK ditekan, misalnya mengosongkan form atau berpindah halaman
            dialog.dismiss()
            // Selesaikan Activity dan kembali ke halaman sebelumnya

            startActivity(Intent(requireContext(),VerifActivity::class.java))
        })

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE_LOGO = 1
        private const val REQUEST_IMAGE_PICK_LOGO = 2
        private const val REQUEST_IMAGE_CAPTURE_KTP = 3
        private const val REQUEST_IMAGE_PICK_KTP = 4
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
}