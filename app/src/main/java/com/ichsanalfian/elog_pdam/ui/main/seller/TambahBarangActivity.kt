package com.ichsanalfian.elog_pdam.ui.main.seller

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ichsanalfian.elog_pdam.databinding.ActivityTambahBarangBinding
import java.io.FileNotFoundException
import java.io.InputStream

class TambahBarangActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahBarangBinding
    private var selectedImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTambahBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Cek izin kamera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Jika izin belum diberikan, minta izin kamera secara dinamis
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            // Jika izin sudah diberikan, Anda dapat membuka kamera
        }


        binding.buttonUploadFoto.setOnClickListener {
            // Memanggil dialog untuk memilih sumber foto (kamera atau galeri)
            showImagePickerDialog()
        }

        binding.buttonSimpan.setOnClickListener {
            // Ambil data dari input dan foto yang telah diunggah
            val nama = binding.editTextNama.text.toString()
            val merk = binding.editTextMerk.text.toString()
            val harga = binding.editTextHarga.text.toString()
            val satuan = binding.editTextSatuan.text.toString()
            val stok = binding.editTextStok.text.toString()
            val kategori = binding.editTextKategori.text.toString()
            val ukuran = binding.editTextUkuran.text.toString()
            val deskripsi = binding.editTextDeskripsi.text.toString()

            // Lakukan penyimpanan data ke database atau tindakan lain yang sesuai
            // ...
            // Jika perlu, Anda juga dapat mengunggah foto ke server di sini
            // ...
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
                }
                REQUEST_IMAGE_PICK -> {
                    val imageUri: Uri? = data?.data
                    if (imageUri != null) {
                        try {
                            val imageStream: InputStream? = contentResolver.openInputStream(imageUri)
                            selectedImage = BitmapFactory.decodeStream(imageStream)
                            binding.imageViewFoto.setImageBitmap(selectedImage)
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
        })

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
        private const val REQUEST_IMAGE_PICK = 2
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }

}
