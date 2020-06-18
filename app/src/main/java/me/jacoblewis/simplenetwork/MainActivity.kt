package me.jacoblewis.simplenetwork

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.network.Network
import com.example.network.body.FormDataBody
import com.example.network.models.FormItem
import com.example.simplenetwork.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.net.URISyntaxException


/**
 * A Sample / Testing Activity
 *
 * Not part of the library
 */
class MainActivity : AppCompatActivity() {
    val net = Network.post(
        "https://sample-api-jkl.herokuapp.com/users", FormDataBody(
            mapOf(
                "age" to FormItem.Text("23"),
                "name" to FormItem.Text("Jake")
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        getName()

        btn_pick_file.setOnClickListener {
            showFileChooser()
        }
    }

    fun getName() {

        net.enqueue({
//            val bitmap = BitmapFactory.decodeByteArray(it.body, 0, it.body?.size ?: 0)
//            imageView.setImageBitmap(bitmap)
//            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            text1.text = it.bodyAsString
            getName()
            Log.i("DONE", "Net Complete")
        }, {
            Log.i("PROGRESS", "PROGRESS: $it")
        })
    }

    fun uploadImage(file: File) {
        val net = Network.put(
            "https://sample-api-jkl.herokuapp.com/dogs", body = FormDataBody(
                mapOf(
                    "photo" to FormItem.File(file, "image/jpeg"),
                    "name" to FormItem.Text("Jake")
                )
            )
        )
        net.enqueue({
            val bitmap = BitmapFactory.decodeByteArray(it.body, 0, it.body?.size ?: 0)
            imageView.setImageBitmap(bitmap)
        }, {
            Log.i("PROGRESS", "PROGRESS: $it")
        })
    }

    val REQUEST_CODE = 1

    fun showFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)

        // Update with mime types
        intent.type = "*/*"

        // Update with additional mime types here using a String[].
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg"))

        // Only pick openable and local files. Theoretically we could pull files from google drive
        // or other applications that have networked files, but that's unnecessary for this example.
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)

        // REQUEST_CODE = <some-integer>
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE -> if (resultCode == Activity.RESULT_OK) {
                // Get the Uri of the selected file
                val uri: Uri = data?.data!!
                // Get the path
                val path: String = getPath(this, uri)!!
                // Get the file instance
                val file = File(path)
                Toast.makeText(this, "${file.absolutePath}", Toast.LENGTH_SHORT).show()
                val bitmap = getBitmapFromUri(uri)
                val tempFile = createFileFromBitmap(bitmap)
//                val tempFileBytes = tempFile.readBytes().toString(Charsets.UTF_32).toByteArray()
//                val bitmap2 = BitmapFactory.decodeByteArray(tempFileBytes, 0, tempFileBytes.size ?: 0)
//                imageView.setImageBitmap(bitmap2)
                uploadImage(tempFile)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun createFileFromBitmap(bitmap: Bitmap): File {
        //create a file to write bitmap data
        val f = File(cacheDir, "tempimg.jpg")
        f.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos)
        val bitmapdata = bos.toByteArray()

        //write the bytes in file
        val fos = FileOutputStream(f)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
        return f
    }

    @Throws(IOException::class)
    private fun getBitmapFromUri(uri: Uri): Bitmap {
        val parcelFileDescriptor: ParcelFileDescriptor =
            applicationContext.contentResolver.openFileDescriptor(uri, "r")!!
        val fileDescriptor: FileDescriptor = parcelFileDescriptor.fileDescriptor
        val image: Bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }

    @Throws(URISyntaxException::class)
    fun getPath(context: Context, uri: Uri): String? {
        // Obtain a cursor with information regarding this uri

        // Obtain a cursor with information regarding this uri
        val cursor =
            contentResolver.query(uri, null, null, null, null)

        if (cursor!!.count <= 0) {
            cursor!!.close()
            throw IllegalArgumentException("Can't obtain file name, cursor is empty")
        }

        cursor!!.moveToFirst()

        val fileName =
            cursor!!.getString(cursor!!.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))

        cursor!!.close()

        return fileName
    }
}