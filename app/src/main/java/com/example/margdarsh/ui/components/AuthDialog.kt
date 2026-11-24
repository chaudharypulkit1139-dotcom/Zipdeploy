package com.example.margdarsh.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.margdarsh.data.StudentAccount
import com.example.margdarsh.ui.theme.ForestGreen
import com.example.margdarsh.ui.theme.GoldAccent
import com.example.margdarsh.ui.theme.InkTextPrimary
import com.example.margdarsh.ui.theme.InkTextSecondary
import com.example.margdarsh.ui.theme.MargNavy
import com.example.margdarsh.ui.theme.PaperBackground
import com.example.margdarsh.ui.theme.PaperSurface
import com.example.margdarsh.ui.theme.WarmClay

@Composable
fun AuthDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onRegister: (name: String, phone: String, email: String, targetClass: String, password: String) -> Result<StudentAccount>,
    onLogin: (phoneOrEmail: String, password: String) -> Result<StudentAccount>,
    currentAccount: StudentAccount? = null
) {
    if (!isOpen) return

    val context = LocalContext.current
    var isSignUpMode by remember { mutableStateOf(currentAccount == null) }
    var name by remember { mutableStateOf(currentAccount?.name ?: "") }
    var phone by remember { mutableStateOf(currentAccount?.phoneNumber ?: "") }
    var email by remember { mutableStateOf(currentAccount?.email ?: "") }
    var selectedClass by remember { mutableStateOf(currentAccount?.studentClass ?: "10") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var classMenuExpanded by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            color = PaperSurface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(ForestGreen.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isSignUpMode) Icons.Default.Badge else Icons.Default.Lock,
                                contentDescription = null,
                                tint = ForestGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isSignUpMode) "Student Sign Up" else "Student Login",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MargNavy
                            )
                            Text(
                                text = if (isSignUpMode) "Create account to access tests & leaderboard" else "Sign in with your phone or email",
                                style = MaterialTheme.typography.bodySmall,
                                color = InkTextSecondary
                            )
                        }
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("auth_dialog_close")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = InkTextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Error or Success Banner
                errorMessage?.let { msg ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(WarmClay.copy(alpha = 0.12f))
                            .border(1.dp, WarmClay, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(text = msg, style = MaterialTheme.typography.bodySmall, color = WarmClay)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                successMessage?.let { msg ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(ForestGreen.copy(alpha = 0.12f))
                            .border(1.dp, ForestGreen, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Text(text = msg, style = MaterialTheme.typography.bodySmall, color = ForestGreen)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (isSignUpMode) {
                    // Full Name
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Full Name *") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_input_name"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    // Target Class Selector
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = "Class $selectedClass",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Class / Grade *") },
                            leadingIcon = { Icon(Icons.Default.School, contentDescription = null) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { classMenuExpanded = true }
                                .testTag("auth_input_class")
                        )
                        DropdownMenu(
                            expanded = classMenuExpanded,
                            onDismissRequest = { classMenuExpanded = false }
                        ) {
                            listOf("8", "9", "10", "11", "12").forEach { c ->
                                DropdownMenuItem(
                                    text = { Text("Class $c") },
                                    onClick = {
                                        selectedClass = c
                                        classMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Phone / Email
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(if (isSignUpMode) "10-Digit Mobile Number *" else "Phone Number or Email *") },
                    leadingIcon = { Icon(if (isSignUpMode || phone.all { it.isDigit() }) Icons.Default.Phone else Icons.Default.Email, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = if (isSignUpMode) KeyboardType.Phone else KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_input_phone"),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))

                if (isSignUpMode) {
                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address *") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_input_email"),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password *") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_input_password"),
                    singleLine = true
                )

                if (!isSignUpMode) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { showForgotPasswordDialog = true },
                            modifier = Modifier.testTag("auth_forgot_password_btn")
                        ) {
                            Text(
                                text = "Forgot Password?",
                                style = MaterialTheme.typography.labelMedium,
                                color = WarmClay
                            )
                        }
                    }
                } else {
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Submit Action Button
                Button(
                    onClick = {
                        errorMessage = null
                        if (isSignUpMode) {
                            val res = onRegister(name, phone, email, selectedClass, password)
                            if (res.isSuccess) {
                                successMessage = "Account created successfully!"
                                onDismiss()
                            } else {
                                errorMessage = res.exceptionOrNull()?.message ?: "Sign up failed"
                            }
                        } else {
                            val res = onLogin(phone, password)
                            if (res.isSuccess) {
                                successMessage = "Logged in successfully!"
                                onDismiss()
                            } else {
                                errorMessage = res.exceptionOrNull()?.message ?: "Login failed"
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("auth_submit_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (isSignUpMode) "Create Student Account" else "Log In",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PaperBackground
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Toggle Mode
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isSignUpMode) "Already have an account?" else "Don't have an account?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkTextSecondary
                    )
                    TextButton(
                        onClick = {
                            isSignUpMode = !isSignUpMode
                            errorMessage = null
                            successMessage = null
                        },
                        modifier = Modifier.testTag("auth_toggle_mode_btn")
                    ) {
                        Text(
                            text = if (isSignUpMode) "Sign In" else "Sign Up",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen
                        )
                    }
                }
            }
        }
    }

    // Forgot Password Dialog
    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = {
                Text("Password Reset Assistance", fontWeight = FontWeight.Bold, color = MargNavy)
            },
            text = {
                Column {
                    Text(
                        text = "For security and identity verification, password resets are processed directly by counselor administration.\n\nPlease contact our official desk with your registered phone number:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = InkTextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(PaperBackground)
                            .border(1.dp, ForestGreen.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Email: margdarsh21@gmail.com",
                            fontWeight = FontWeight.Bold,
                            color = ForestGreen,
                            fontSize = 15.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:margdarsh21@gmail.com")
                            putExtra(Intent.EXTRA_SUBJECT, "Password Reset Request - MargDarsh Student")
                            putExtra(Intent.EXTRA_TEXT, "Hello MargDarsh Support,\n\nI need assistance resetting the password for my MargDarsh student account.\nRegistered Phone: $phone\nStudent Name: $name\n\nThank you.")
                        }
                        try {
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                        showForgotPasswordDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
                    modifier = Modifier.testTag("forgot_password_send_email_btn")
                ) {
                    Text("Send Email (margdarsh21@gmail.com)")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showForgotPasswordDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}
