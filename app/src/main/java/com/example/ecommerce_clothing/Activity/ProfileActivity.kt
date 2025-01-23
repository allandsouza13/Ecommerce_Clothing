package com.example.ecommerce_clothing

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.ecommerce_clothing.Activity.BaseActivity
import com.example.ecommerce_clothing.Activity.RegistrationActivity

class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreen(
                onSignOutClick = {
                    startActivity(Intent(this, RegistrationActivity::class.java))
                    finish()
                }
            )
        }
    }
}

@Composable
fun ProfileScreen(onSignOutClick: () -> Unit) {
    var name by remember { mutableStateOf("John Doe") }
    var email by remember { mutableStateOf("john.doe@example.com") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            profileImageUri = uri
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .clickable { galleryLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(profileImageUri),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(text = "Add Photo", color = MaterialTheme.colorScheme.onPrimary)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Name Field
        EditableField(label = "Name", value = name) { newValue -> name = newValue }

        // Email Field
        EditableField(label = "Email", value = email) { newValue -> email = newValue }

        Spacer(modifier = Modifier.height(24.dp))

        // Sign Out Button with Confirmation Dialog
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(onClick = { onSignOutClick(); showDialog = false }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("No")
                    }
                },
                title = { Text("Sign Out") },
                text = { Text("Are you sure you want to sign out?") }
            )
        }

        Button(onClick = { showDialog = true }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Sign Out", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EditableField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(text = label, fontWeight = FontWeight.Bold)
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// MainActivity (or any other screen where you want to show the BottomMenu)
@Composable
fun MainActivityScreen(onCartClick: () -> Unit, onProfileClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Your screen content here

        // Bottom Navigation Bar
        BottomMenu(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onItemClick = { item ->
                when (item) {
                    "Cart" -> onCartClick()
                    "Profile" -> onProfileClick() // Added this line
                    else -> {}
                }
            }
        )
    }
}

@Composable
fun BottomMenu(modifier: Modifier = Modifier, onItemClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(24.dp) // Rounded edges for the bar
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        BottomMenuItem(
            icon = painterResource(id = R.drawable.btn_2),
            text = "Cart",
            onItemClick = { onItemClick("Cart") }
        )
        BottomMenuItem(
            icon = painterResource(id = R.drawable.btn_5),
            text = "Profile",
            onItemClick = { onItemClick("Profile") }  // Profile click handler
        )
    }
}

@Composable
fun BottomMenuItem(icon: Painter, text: String, onItemClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onItemClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 10.sp
        )
    }
}
