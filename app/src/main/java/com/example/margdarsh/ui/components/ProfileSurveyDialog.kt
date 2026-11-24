package com.example.margdarsh.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.margdarsh.R
import com.example.margdarsh.data.StudentProfile
import com.example.margdarsh.ui.theme.ForestGreen
import com.example.margdarsh.ui.theme.PaperBackground
import com.example.margdarsh.ui.theme.WarmClay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSurveyDialog(
    initialProfile: StudentProfile,
    onDismiss: () -> Unit,
    onSave: (StudentProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    // 2-Step Flow:
    // Step 1: Sign in with Google (Clean, dedicated sign-in view)
    // Step 2: Additional details (Phone number, Student name, Parent name, Class, School, City)
    val hasInitialEmail = initialProfile.email.isNotBlank()
    var currentStep by remember { mutableIntStateOf(if (hasInitialEmail) 2 else 1) }

    var studentName by remember { mutableStateOf(initialProfile.studentName) }
    var parentName by remember { mutableStateOf(initialProfile.parentName) }
    var phoneNumber by remember { mutableStateOf(initialProfile.phoneNumber) }
    var email by remember { mutableStateOf(initialProfile.email) }
    var isGoogleVerified by remember {
        mutableStateOf(
            initialProfile.isGoogleVerified || (initialProfile.email.isNotBlank() && initialProfile.email.endsWith("@gmail.com"))
        )
    }
    var studentClass by remember { mutableStateOf(initialProfile.studentClass) }
    var schoolName by remember { mutableStateOf(initialProfile.schoolName) }
    var city by remember { mutableStateOf(initialProfile.city) }

    val classOptions = listOf("Class 8", "Class 9", "Class 10", "Class 11", "Class 12", "Dropper / Target Exam")
    var classDropdownExpanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Dialog(
        onDismissRequest = {
            // Cannot dismiss if required details are empty (Unskippable initial registration gate)
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .systemBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = RoundedCornerShape(26.dp),
                color = PaperBackground,
                border = BorderStroke(1.5.dp, ForestGreen.copy(alpha = 0.5f)),
                shadowElevation = 12.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp)
                    .testTag("profile_survey_dialog")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    // MargDarsh Official Brand Badge
                    MargdarshLogoCompact(
                        iconSize = 28.dp,
                        textSize = 18,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    // Header with Counselor Badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.dr_bb_panchal_counsellor_1789197215041),
                            contentDescription = "Dr. B.B. Panchal",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(54.dp)
                                .clip(CircleShape)
                                .background(ForestGreen)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Dr. B.B. Panchal",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = ForestGreen
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = "Verified",
                                    tint = ForestGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "Senior Career Counsellor • Bodhami Registered",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Step Indicator Badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = ForestGreen.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = if (currentStep == 1) "STEP 1 OF 2 : GOOGLE SIGN IN" else "STEP 2 OF 2 : CANDIDATE DETAILS",
                                style = MaterialTheme.typography.labelSmall,
                                color = ForestGreen,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        // Stepper dots
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                                shape = CircleShape,
                                color = if (currentStep == 1) ForestGreen else ForestGreen.copy(alpha = 0.3f),
                                modifier = Modifier.size(8.dp)
                            ) {}
                            Surface(
                                shape = CircleShape,
                                color = if (currentStep == 2) ForestGreen else ForestGreen.copy(alpha = 0.3f),
                                modifier = Modifier.size(8.dp)
                            ) {}
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    AnimatedContent(
                        targetState = currentStep,
                        transitionSpec = {
                            if (targetState > initialState) {
                                slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                            } else {
                                slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
                            }
                        },
                        label = "registration_steps_animation"
                    ) { step ->
                        if (step == 1) {
                            // =========================================================================
                            // STEP 1: SIGN IN WITH GOOGLE ONLY SCREEN
                            // =========================================================================
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Sign in to Continue",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = ForestGreen,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "To access the official Margdarsh Psychometric Evaluation and receive verified guidance from Dr. B.B. Panchal, please sign in with your Google Account.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 18.sp
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                // Google Sign-In Primary Widget
                                GoogleSignInSection(
                                    email = email,
                                    isGoogleVerified = isGoogleVerified,
                                    onEmailChange = { newEmail, isGoogle ->
                                        email = newEmail
                                        isGoogleVerified = isGoogle
                                        errorMessage = ""
                                        // Auto-advance to Step 2 when sign-in succeeds
                                        if (newEmail.isNotBlank()) {
                                            currentStep = 2
                                        }
                                    },
                                    onAutoFillName = { suggestedName ->
                                        if (studentName.isBlank()) {
                                            studentName = suggestedName
                                        }
                                    }
                                )

                                if (errorMessage.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = errorMessage,
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // If email is already selected or typed, provide explicit Next button
                                if (email.isNotBlank()) {
                                    Button(
                                        onClick = {
                                            if (email.contains("@")) {
                                                errorMessage = ""
                                                currentStep = 2
                                            } else {
                                                errorMessage = "Please enter a valid Google email address."
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .testTag("step1_next_button"),
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = ForestGreen,
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Text("Continue to Details", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Security note
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color.Black.copy(alpha = 0.03f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Lock,
                                            contentDescription = null,
                                            tint = ForestGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Your email is encrypted and strictly used for sharing your customized career roadmap dossier.",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 11.sp
                                        )
                                    }
                                }
                            }
                        } else {
                            // =========================================================================
                            // STEP 2: ADDITIONAL DETAILS (PHONE NUMBER, NAMES, CLASS, ETC.)
                            // =========================================================================
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Candidate Details",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = ForestGreen,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Please provide your contact number and academic details to complete the student dossier.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 18.sp
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                // Linked account chip with change option
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = ForestGreen.copy(alpha = 0.08f),
                                    border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.3f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.CheckCircle,
                                                contentDescription = null,
                                                tint = ForestGreen,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = email,
                                                style = MaterialTheme.typography.bodySmall,
                                                fontWeight = FontWeight.SemiBold,
                                                color = ForestGreen
                                            )
                                        }
                                        TextButton(
                                            onClick = { currentStep = 1 },
                                            modifier = Modifier.testTag("step2_change_account_button")
                                        ) {
                                            Text(
                                                text = "Switch Account",
                                                color = WarmClay,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // 1. Contact / WhatsApp Phone (Prominent per user request)
                                OutlinedTextField(
                                    value = phoneNumber,
                                    onValueChange = {
                                        phoneNumber = it
                                        errorMessage = ""
                                    },
                                    label = { Text("WhatsApp / Contact Phone Number *") },
                                    placeholder = { Text("e.g. 9876543210") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Phone, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
                                    },
                                    supportingText = {
                                        Text("10-digit mobile number to receive career roadmap", style = MaterialTheme.typography.bodySmall)
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                                    modifier = Modifier.fillMaxWidth().testTag("profile_input_phone"),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = ForestGreen,
                                        focusedLabelColor = ForestGreen
                                    )
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // 2. Student Name (Mandatory)
                                OutlinedTextField(
                                    value = studentName,
                                    onValueChange = { studentName = it; errorMessage = "" },
                                    label = { Text("Student Full Name *") },
                                    leadingIcon = {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(20.dp))
                                    },
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                    modifier = Modifier.fillMaxWidth().testTag("profile_input_student_name"),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = ForestGreen,
                                        focusedLabelColor = ForestGreen
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // 3. Parent Name (Mandatory)
                                OutlinedTextField(
                                    value = parentName,
                                    onValueChange = { parentName = it; errorMessage = "" },
                                    label = { Text("Parent / Guardian Name *") },
                                    leadingIcon = {
                                        Icon(Icons.Default.FamilyRestroom, contentDescription = null, tint = WarmClay, modifier = Modifier.size(20.dp))
                                    },
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                    modifier = Modifier.fillMaxWidth().testTag("profile_input_parent_name"),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = WarmClay,
                                        focusedLabelColor = WarmClay
                                    )
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                // 4. Class Selection Dropdown
                                ExposedDropdownMenuBox(
                                    expanded = classDropdownExpanded,
                                    onExpandedChange = { classDropdownExpanded = it },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                        value = studentClass,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Current Class / Grade *") },
                                        leadingIcon = {
                                            Icon(Icons.Default.School, contentDescription = null, tint = WarmClay, modifier = Modifier.size(20.dp))
                                        },
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = classDropdownExpanded) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                                            .testTag("profile_input_class"),
                                        shape = RoundedCornerShape(12.dp)
                                    )

                                    ExposedDropdownMenu(
                                        expanded = classDropdownExpanded,
                                        onDismissRequest = { classDropdownExpanded = false }
                                    ) {
                                        classOptions.forEach { option ->
                                            DropdownMenuItem(
                                                text = { Text(option) },
                                                onClick = {
                                                    studentClass = option
                                                    classDropdownExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // 5. School & City in a row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedTextField(
                                        value = schoolName,
                                        onValueChange = { schoolName = it },
                                        label = { Text("School") },
                                        placeholder = { Text("e.g. DPS") },
                                        leadingIcon = {
                                            Icon(Icons.Default.Badge, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(18.dp))
                                        },
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                        modifier = Modifier.weight(1.1f).testTag("profile_input_school"),
                                        shape = RoundedCornerShape(12.dp),
                                        singleLine = true
                                    )

                                    OutlinedTextField(
                                        value = city,
                                        onValueChange = { city = it },
                                        label = { Text("City") },
                                        placeholder = { Text("e.g. Indore") },
                                        leadingIcon = {
                                            Icon(Icons.Default.LocationCity, contentDescription = null, tint = ForestGreen, modifier = Modifier.size(18.dp))
                                        },
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                                        modifier = Modifier.weight(1f).testTag("profile_input_city"),
                                        shape = RoundedCornerShape(12.dp),
                                        singleLine = true
                                    )
                                }

                                if (errorMessage.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = errorMessage,
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Spacer(modifier = Modifier.height(20.dp))

                                // Actions: Back to Step 1 & Complete Registration Button
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedButton(
                                        onClick = { currentStep = 1 },
                                        modifier = Modifier
                                            .height(50.dp)
                                            .testTag("step2_back_button"),
                                        shape = CircleShape,
                                        border = BorderStroke(1.dp, ForestGreen.copy(alpha = 0.5f))
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = ForestGreen, modifier = Modifier.size(16.dp))
                                    }

                                    Button(
                                        onClick = {
                                            keyboardController?.hide()

                                            val digitsOnly = phoneNumber.filter { it.isDigit() }

                                            if (email.isBlank() || !email.contains("@")) {
                                                errorMessage = "Google email is required. Please go back and sign in."
                                                return@Button
                                            }
                                            if (phoneNumber.isBlank() || digitsOnly.length < 10) {
                                                errorMessage = "Please enter a valid 10-digit mobile or WhatsApp number."
                                                return@Button
                                            }
                                            if (studentName.isBlank()) {
                                                errorMessage = "Please enter student name."
                                                return@Button
                                            }
                                            if (parentName.isBlank()) {
                                                errorMessage = "Please enter parent/guardian name."
                                                return@Button
                                            }

                                            val cleanFormattedPhone = if (phoneNumber.trim().startsWith("+")) {
                                                phoneNumber.trim()
                                            } else if (digitsOnly.length == 10) {
                                                "+91 $digitsOnly"
                                            } else {
                                                phoneNumber.trim()
                                            }

                                            onSave(
                                                StudentProfile(
                                                    studentName = studentName.trim(),
                                                    parentName = parentName.trim(),
                                                    phoneNumber = cleanFormattedPhone,
                                                    email = email.trim(),
                                                    studentClass = studentClass.trim(),
                                                    schoolName = schoolName.trim(),
                                                    city = city.trim(),
                                                    isGoogleVerified = isGoogleVerified
                                                )
                                            )
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(50.dp)
                                            .testTag("save_profile_button"),
                                        shape = CircleShape,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = ForestGreen,
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Register & Unlock", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "By registering, you and your guardian agree to our Terms of Service & Privacy Policy.",
                                    fontSize = 10.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(14.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
