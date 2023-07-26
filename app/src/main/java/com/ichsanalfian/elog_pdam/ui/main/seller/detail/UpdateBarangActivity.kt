package com.ichsanalfian.elog_pdam.ui.main.seller.detail

import android.Manifest
import android.app.Activity
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
import com.bumptech.glide.Glide
import com.ichsanalfian.elog_pdam.BuildConfig
import com.ichsanalfian.elog_pdam.databinding.ActivityUpdateBarangBinding
import com.ichsanalfian.elog_pdam.ui.main.seller.add.TambahBarangActivity
import java.io.*

class UpdateBarangActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBarangBinding
    private lateinit var selectedImageFile: File
    private var selectedImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from intent extras
        val namaProduk = intent.getStringExtra(EXTRA_NAMA)
        val merkProduk = intent.getStringExtra(EXTRA_MERK)
        val kodeProduk = intent.getStringExtra(EXTRA_KODE)
        val hargaProduk = intent.getStringExtra(EXTRA_HARGA)
        val satuanProduk = intent.getStringExtra(EXTRA_SATUAN)
        val stokProduk = intent.getStringExtra(EXTRA_STOK)
        val kategoriProduk = intent.getStringExtra(EXTRA_KATEGORI)
        val ukuranProduk = intent.getStringExtra(EXTRA_UKURAN)
        val deskripsiProduk = intent.getStringExtra(EXTRA_DESKRIPSI)
        val gambarProduk = intent.getStringExtra(EXTRA_GAMBAR) //TODO Tambahan
        val idProduk = intent.getIntExtra(EXTRA_ID, 0)

        // Fill the input fields and image view with the data from the intent
        binding.apply {
            editTextNama.setText(namaProduk)
            editTextMerk.setText(merkProduk)
            editTextKode.setText(kodeProduk)
            editTextHarga.setText(hargaProduk)
            editTextSatuan.setText(satuanProduk)
            editTextStok.setText(stokProduk)
            editTextKategori.setText(kategoriProduk)
            editTextUkuran.setText(ukuranProduk)
            editTextDeskripsi.setText(deskripsiProduk)

            //TODO Tambahan
            Glide.with(applicationContext).load("${BuildConfig.BASE_URL}${gambarProduk}")
                .into(imageViewFoto)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Jika izin belum diberikan, minta izin kamera secara dinamis
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                UpdateBarangActivity.CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Jika izin sudah diberikan, Anda dapat membuka kamera
        }


        binding.buttonUploadFoto.setOnClickListener {
            // Memanggil dialog untuk memilih sumber foto (kamera atau galeri)
            showImagePickerDialog()
        }
    }
    private fun showImagePickerDialog() {
        val items = arrayOf<CharSequence>("Kamera", "Galeri")
        val builder = AlertDialog.Builder(this)
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
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                            Toast.makeText(this, "Gambar tidak ditemukan!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
    private fun saveImageToFile(bitmap: Bitmap?): File {
        // Create a file in the cache directory to store the image
        val imgName = "${binding.editTextNama.text.toString()}__${binding.editTextKode.text.toString()}.jpg" //TODO Diganti
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


    }
}
