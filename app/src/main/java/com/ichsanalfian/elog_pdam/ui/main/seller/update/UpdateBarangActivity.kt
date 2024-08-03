package com.ichsanalfian.elog_pdam.ui.main.seller.update

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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ichsanalfian.elog_pdam.BuildConfig
import com.ichsanalfian.elog_pdam.databinding.ActivityUpdateBarangBinding
import com.ichsanalfian.elog_pdam.model.Barang
import com.ichsanalfian.elog_pdam.ui.main.seller.SellerActivity
import com.ichsanalfian.elog_pdam.viewModel.ViewModelFactory
import com.ichsanalfian.elog_pdam.viewModel.SellerViewModel
import java.io.*

class UpdateBarangActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBarangBinding
    private var selectedImage: Bitmap? = null
    private lateinit var sellerViewModel: SellerViewModel
    private var selectedImageFile: File? = null
    private var isImageChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sellerViewModel = ViewModelProvider(this, ViewModelFactory())[SellerViewModel::class.java]

        // Get data from intent extras
        val namaProduk = intent.getStringExtra(EXTRA_NAMA)
        val merkProduk = intent.getStringExtra(EXTRA_MERK)
        val kodeProduk = intent.getStringExtra(EXTRA_KODE)
        val hargaProduk = intent.getIntExtra(EXTRA_HARGA, 0)
        val satuanProduk = intent.getStringExtra(EXTRA_SATUAN)
        val stokProduk = intent.getStringExtra(EXTRA_STOK)
        val kategoriProduk = intent.getStringExtra(EXTRA_KATEGORI)
        val ukuranProduk = intent.getStringExtra(EXTRA_UKURAN)
        val deskripsiProduk = intent.getStringExtra(EXTRA_DESKRIPSI)
        val gambarProduk = intent.getStringExtra(EXTRA_GAMBAR) //TODO Tambahan
        val idProduk = intent.getIntExtra(EXTRA_ID,0)
        val idSeller = intent.getStringExtra(EXTRA_ID_SELLER)

        // Fill the input fields and image view with the data from the intent
        binding.apply {
            editTextNama.setText(namaProduk)
            editTextMerk.setText(merkProduk)
            editTextKode.setText(kodeProduk)
            editTextHarga.setText(hargaProduk.toString())
            editTextSatuan.setText(satuanProduk)
            editTextStok.setText(stokProduk)
            editTextKategori.setText(kategoriProduk)
            editTextUkuran.setText(ukuranProduk)
            editTextDeskripsi.setText(deskripsiProduk)

            //TODO Tambahan
            Glide.with(applicationContext).load("${BuildConfig.BASE_URL}uploads/barang/${gambarProduk}")
                .into(imageViewFoto)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Jika izin belum diberikan, minta izin kamera secara dinamis
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Jika izin sudah diberikan, Anda dapat membuka kamera
        }
        // Load existing image (if any)
        binding.buttonUploadFoto.setOnClickListener {
            // Memanggil dialog untuk memilih sumber foto (kamera atau galeri)
            showImagePickerDialog()
        }

        binding.buttonSimpan.setOnClickListener {
            // Ambil data dari input dan foto yang telah diunggah
            val nama = binding.editTextNama.text.toString()
            val merk = binding.editTextMerk.text.toString()
            val kode = binding.editTextKode.text.toString()
            val harga = binding.editTextHarga.text.toString().toInt()
            val satuan = binding.editTextSatuan.text.toString()
            val stok = binding.editTextStok.text.toString().toInt()
            val kategori = binding.editTextKategori.text.toString()
            val ukuran = binding.editTextUkuran.text.toString()
            val deskripsi = binding.editTextDeskripsi.text.toString()
            val barang: Barang
            // ...
            // Panggil fungsi updateBarang pada sellerViewModel dengan menyertakan ID barang

            if (isImageChanged){
                barang = Barang(idProduk, nama, merk, harga, satuan, kode, stok, kategori, ukuran, deskripsi, selectedImageFile?.name, idSeller) // Menggunakan nama file yang dipilih
            } else{
                barang = Barang(idProduk, nama, merk, harga, satuan, kode, stok, kategori, ukuran, deskripsi, gambarProduk, idSeller) // Menggunakan nama file yang dipilih
            }
            sellerViewModel.updateBarang(barang, selectedImageFile)
            println("WKOWKOWKOWKOW $gambarProduk")
            showSuccessDialog()
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Jika izin kamera diberikan, buka kamera

            } else {
                Toast.makeText(this, "Izin kamera dibutuhkan untuk mengakses kamera", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun saveImageToFile(bitmap: Bitmap?): File {
        // Create a file in the cache directory to store the image
        val imgName = "${binding.editTextNama.text}__${binding.editTextKode.text}.jpg" //TODO Diganti
        val file = File(cacheDir, imgName)
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



    private fun showImagePickerDialog() {
        val items = arrayOf<CharSequence>("Kamera", "Galeri")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Pilih Sumber Foto")
        builder.setItems(items) { _, item ->
            when (item) {
                0 -> takePictureFromCamera()
                1 -> pickImageFromGallery()
            }
        }
        builder.show()
    }

    private fun takePictureFromCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    private fun pickImageFromGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    selectedImage = imageBitmap
                    binding.imageViewFoto.setImageBitmap(selectedImage)

                    // Save the selected image to a file
                    selectedImageFile = saveImageToFile(selectedImage)
                    isImageChanged = true
                }
                REQUEST_IMAGE_PICK -> {
                    val imageUri: Uri? = data?.data
                    if (imageUri != null) {
                        try {
                            val imageStream: InputStream? = contentResolver.openInputStream(imageUri)
                            selectedImage = BitmapFactory.decodeStream(imageStream)
                            binding.imageViewFoto.setImageBitmap(selectedImage)

                            // Save the selected image to a file
                            selectedImageFile = saveImageToFile(selectedImage)
                            isImageChanged = true

                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                            Toast.makeText(this, "Gambar tidak ditemukan!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun showSuccessDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("YEAY")
        dialogBuilder.setMessage("Barang berhasil ditambahkan!")
        dialogBuilder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, _ ->
            // Tindakan setelah tombol OK ditekan, misalnya mengosongkan form atau berpindah halaman
            dialog.dismiss()
            // Selesaikan Activity dan kembali ke halaman sebelumnya
            finish()
            startActivity(Intent(this, SellerActivity::class.java))
        })

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_IMAGE_PICK = 2
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAMA = "extra_nama"
        const val EXTRA_MERK = "extra_merk"
        const val EXTRA_KODE = "extra_kode"
        const val EXTRA_HARGA = "extra_harga"
        const val EXTRA_SATUAN = "extra_satuan"
        const val EXTRA_STOK = "extra_stok"
        const val EXTRA_KATEGORI = "extra_kategori"
        const val EXTRA_UKURAN = "extra_ukuran"
        const val EXTRA_DESKRIPSI = "extra_deskripsi"
        const val EXTRA_GAMBAR = "extra_gambar" //TODO Tambahan
        const val EXTRA_ID_SELLER = "extra_id_seller"
    }
}
