package com.vogella.android.socialproject.layouts

import android.annotation.SuppressLint
import android.net.Uri
import android.provider.ContactsContract.Contacts.Photo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.vogella.android.socialproject.FireBase.Database
import com.vogella.android.socialproject.FireBase.Database.db
import com.vogella.android.socialproject.R
import kotlinx.coroutines.tasks.await

@Composable
fun ProfileEdit(navController: NavController){
    var ho by remember { mutableStateOf("") }
    var ten by remember { mutableStateOf("") }
    var gioiTinh by remember { mutableStateOf("") }
    val avatar = if (gioiTinh == "Nam") R.drawable.nam else if (gioiTinh == "Nữ") R.drawable.nu else R.drawable.khac
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    Column(){
        Row(modifier =  Modifier.fillMaxWidth()) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
            ) {
                Image(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(6.dp))
            Text(text="Chỉnh sửa trang cá nhân",modifier=Modifier.offset(y=10.dp),
                fontSize = 20.sp, fontWeight = FontWeight.ExtraBold
            ,color= colorResource(R.color.pink)
            )

        }
        Divider(
            color = colorResource(R.color.pink),
            thickness = 1.dp
        )
        Spacer(Modifier.height(10.dp))
        LazyColumn(modifier=Modifier.fillMaxSize()) {
            item{
                ImageEdit{ uri ->
                    selectedImageUri = uri}
            }
            item{
                TextFieldHo(ho){
                    ho=it
                }
                Spacer(Modifier.height(20.dp))
            }
            item {
                TextFieldTen(ten){ten=it}
                Spacer(Modifier.height(20.dp))
            }
            item{
                TextFieldGioiTinh(gioiTinh){gioiTinh=it}
                Spacer(Modifier.height(20.dp))
            }
            item{
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp), // Thêm khoảng cách bên dưới nếu cần
                    contentAlignment = Alignment.Center // Căn giữa nội dung bên trong Box
                ) {
                    Button(
                        onClick = {
                            updateUserProfile(ho,ten,gioiTinh){
                                navController.popBackStack()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White
                        ),
                        modifier = Modifier
                            .border(
                                BorderStroke(1.dp, color = colorResource(R.color.pink)),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .size(width = 320.dp, height = 37.dp)
                    ) {
                        Text(text = "Lưu thay đổi", color = colorResource(R.color.pink))
                    }
                }
            }
        }
    }
}
@Composable
fun ImageEdit(onImageSelected: (Uri?) -> Unit){
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    val singlePhotoPickerLauncher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            selectedImageUri = uri
            onImageSelected(uri)
        }
    )
    val multiplePhotoPickerLauncher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris ->
            selectedImageUris = uris
        }
    )
    Column(modifier=Modifier.padding(top=15.dp)){
        Row(modifier=Modifier.fillMaxWidth()){
            Text(text="Ảnh đại diện", fontSize = 25.sp)
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    singlePhotoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .width(165.dp)
                    .offset(y = -4.dp)
                    .height(30.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(), // Chiếm toàn bộ không gian của Button
                    contentAlignment = Alignment.CenterEnd // Căn trái nội dung
                ) {
                    Text(
                        text = "Chỉnh sửa",
                        color = colorResource(R.color.pink),
                        fontSize = 12.sp,
                    )
                }
            }
        }
        Spacer(Modifier.height(25.dp))
        GetHinhDaiDienChinhSua()
        Spacer(Modifier.height(25.dp))
        Row(){
            Text(text="Ảnh bìa", fontSize = 25.sp)
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {singlePhotoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                          },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .width(165.dp)
                    .offset(y = -4.dp)
                    .height(30.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(), // Chiếm toàn bộ không gian của Button
                    contentAlignment = Alignment.CenterEnd // Căn trái nội dung
                ) {
                    Text(
                        text = "Chỉnh sửa",
                        color = colorResource(R.color.pink),
                        fontSize = 12.sp,
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        GetHinhBiaChinhSua()
    }
    Spacer(Modifier.height(20.dp))
}
@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun TextFieldHo(ho:String,onHoChange: (String) ->Unit){
    val firestore=FirebaseFirestore.getInstance()
    val userId=FirebaseAuth.getInstance().currentUser?.uid
    LaunchedEffect (Unit){
        userId?.let{
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener {document->
                    if (document != null && document.exists()) {
                        onHoChange(document.getString("ho") ?: "")
                    }
                }
        }
    }
    OutlinedTextField(
        value = ho,
        onValueChange = { newHo->
            onHoChange(newHo)
        },
        label = { Text("Họ") },  // Nhãn nổi phía trên
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp), // Bo góc nhẹ
        colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(R.color.pink),   // Màu viền khi được chọn
            unfocusedBorderColor = Color.Gray  // Màu viền khi không chọn
        )
    )

}
@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun TextFieldTen(ten:String,onTenChange: (String) ->Unit){
    val firestore=FirebaseFirestore.getInstance()
    val userId=FirebaseAuth.getInstance().currentUser?.uid
    LaunchedEffect (Unit){
        userId?.let{
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener {document->
                    if (document != null && document.exists()) {
                        onTenChange(document.getString("ten") ?: "")
                    }
                }
        }
    }
    OutlinedTextField(
        value = ten,
        onValueChange = { newTen->
            onTenChange(newTen)
        },
        label = { Text("Tên") },  // Nhãn nổi phía trên
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp), // Bo góc nhẹ
        colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(R.color.pink),   // Màu viền khi được chọn
            unfocusedBorderColor = Color.Gray  // Màu viền khi không chọn
        )
    )

}
@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun TextFieldGioiTinh(gioiTinh:String,onSexChange: (String) ->Unit) {
    val option= listOf("Nam","Nữ","Khác")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(gioiTinh) }
    val firestore=FirebaseFirestore.getInstance()
    val userId=FirebaseAuth.getInstance().currentUser?.uid
    LaunchedEffect (Unit){
        userId?.let{
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener {document->
                    if (document != null && document.exists()) {
                        onSexChange(document.getString("sex") ?: "Nam")
                        selectedOptionText=document.getString("sex") ?: "Nam"
                    }
                }
        }
    }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().menuAnchor(),
            value = selectedOptionText,
            onValueChange = { newSex->
                onSexChange(newSex)
            },
            label = { Text("Giơi tính") }, // Nhãn nổi phía trên
            readOnly = false,
            trailingIcon = {ExposedDropdownMenuDefaults.TrailingIcon(expanded=expanded)},
            colors = androidx.compose.material3.TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = colorResource(R.color.pink),   // Màu viền khi được chọn
                unfocusedBorderColor = Color.Gray  // Màu viền khi không chọn
            )
        )
        ExposedDropdownMenu(expanded=expanded, onDismissRequest = {expanded=false}){
            option.forEach{selectedOption->
                DropdownMenuItem(
                    text = { Text(selectedOption)},
                    onClick = {
                        selectedOptionText = selectedOption
                        expanded = false
                        onSexChange(selectedOption)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}
@Composable
fun GetHinhDaiDienChinhSua(){
    var avatarUrl by remember { mutableStateOf("") }
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(userId) {
        userId?.let {
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    avatarUrl = document.getString("photoUrl") ?: ""
                }
        }
    }
    Box(modifier = Modifier.fillMaxSize() ) {
        Image(
            painter = rememberAsyncImagePainter(model = avatarUrl),
            contentDescription = "Circular Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(100.dp))
                .border(
                    width = 1.dp, color = Color.Black,
                    shape = RoundedCornerShape(100.dp)
                ).align(Alignment.Center)
        )
    }
}
@Composable
fun GetHinhBiaChinhSua() {
    var backgroundAvatar by remember { mutableStateOf("") }
    val db = FirebaseFirestore.getInstance()
    val docRef = db.collection("users").document(Firebase.auth.currentUser!!.uid)
    docRef.get().addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
        if (documentSnapshot.exists()) {
            backgroundAvatar = documentSnapshot.getString("backgroundAvatar").toString()
        }
    }
    Box(modifier = Modifier.fillMaxSize() ) {
        Image(
            painter = rememberAsyncImagePainter(model = backgroundAvatar),
            contentDescription = "Main Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
    }
}
private fun updateUserProfile(ho: String, ten: String,gioiTinh:String,onUpdateComplete: () -> Unit) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val firestore = FirebaseFirestore.getInstance()
    firestore.collection("users").document(userId)
        .update("ho", ho, "ten", ten,"sex",gioiTinh,)
        .addOnSuccessListener {
            val profileUpdates = userProfileChangeRequest {
                displayName = "$ho$ten"
            }
            FirebaseAuth.getInstance().currentUser?.updateProfile(profileUpdates)

            onUpdateComplete()
        }

}
