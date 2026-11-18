package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ui.components.BidarshoLogoMark
import com.example.ui.components.CanvasHeaderTheme
import com.example.ui.components.MinimalistCanvasHeader
import com.example.ui.components.toPersianDigits
import com.example.ui.theme.DeepTeal
import com.example.ui.theme.MistAqua
import com.example.ui.theme.SageTeal
import com.example.ui.theme.WarmCream
import com.example.ui.theme.WarmGold
import com.example.viewmodel.RegistrationState

@Composable
fun AuthScreen(
    state: RegistrationState,
    onFullNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onOtpChange: (String) -> Unit,
    onRequestOtp: () -> Unit,
    onResendOtp: () -> Unit,
    onBackToForm: () -> Unit,
    onVerifyAndEnter: () -> Unit,
    modifier: Modifier = Modifier
) {
    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = DeepTeal,
        unfocusedBorderColor = SageTeal.copy(alpha = 0.55f),
        focusedLabelColor = DeepTeal,
        unfocusedLabelColor = SageTeal,
        cursorColor = DeepTeal,
        focusedContainerColor = WarmCream,
        unfocusedContainerColor = WarmCream
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCream)
            .imePadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BidarshoLogoMark(size = 52.dp)
                Column {
                    Text(
                        text = "موسسه روانشناسی بیدارشو",
                        style = MaterialTheme.typography.headlineSmall,
                        color = DeepTeal
                    )
                    Text(
                        text = "کوچینگ تخصصی روابط و خودآگاهی",
                        style = MaterialTheme.typography.bodySmall,
                        color = SageTeal
                    )
                }
            }

            MinimalistCanvasHeader(
                theme = CanvasHeaderTheme.AUTH,
                title = "ورود و ثبت‌نام اعضای بیدارشو",
                subtitle = "برای دسترسی به اپیزودها، مدیتیشن‌ها و کلاس‌های آنلاین ابتدا ثبت‌نام کنید"
            )

            AnimatedVisibility(visible = state.isOtpStep) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = WarmGold.copy(alpha = 0.45f),
                    border = BorderStroke(1.5.dp, DeepTeal.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MarkEmailRead,
                            contentDescription = null,
                            tint = DeepTeal,
                            modifier = Modifier.size(28.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "کد تایید به شماره ${state.phoneNumber.toPersianDigits()} ارسال شد:",
                                style = MaterialTheme.typography.labelLarge,
                                color = DeepTeal
                            )
                            Text(
                                text = "کد تایید شما: ${state.generatedOtp.toPersianDigits()}",
                                style = MaterialTheme.typography.titleMedium,
                                color = DeepTeal,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MistAqua.copy(alpha = 0.25f)),
                border = BorderStroke(1.dp, MistAqua),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    if (!state.isOtpStep) {
                        Text(
                            text = "مشخصات فردی خود را وارد کنید",
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepTeal
                        )

                        OutlinedTextField(
                            value = state.fullName,
                            onValueChange = onFullNameChange,
                            label = { Text("نام و نام خانوادگی") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = DeepTeal)
                            },
                            singleLine = true,
                            colors = fieldColors,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_name_input")
                        )

                        OutlinedTextField(
                            value = state.email,
                            onValueChange = onEmailChange,
                            label = { Text("ایمیل") },
                            leadingIcon = {
                                Icon(Icons.Default.Email, contentDescription = null, tint = DeepTeal)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true,
                            colors = fieldColors,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_email_input")
                        )

                        OutlinedTextField(
                            value = state.phoneNumber,
                            onValueChange = onPhoneChange,
                            label = { Text("شماره تماس (مثال: ۰۹۱۲۳۴۵۶۷۸۹)") },
                            leadingIcon = {
                                Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = DeepTeal)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            singleLine = true,
                            colors = fieldColors,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_phone_input")
                        )

                        state.errorMessage?.let { err ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = WarmGold.copy(alpha = 0.5f),
                                border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = err,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = DeepTeal,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                )
                            }
                        }

                        Button(
                            onClick = onRequestOtp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("send_otp_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepTeal,
                                contentColor = WarmCream
                            )
                        ) {
                            Text(
                                text = "ارسال کد تایید به شماره تماس",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    } else {
                        Text(
                            text = "تایید شماره تماس",
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepTeal
                        )
                        Text(
                            text = "کد ۵ رقمی ارسال‌شده برای ${state.fullName} را وارد کنید:",
                            style = MaterialTheme.typography.bodySmall,
                            color = SageTeal
                        )

                        OutlinedTextField(
                            value = state.otpInput,
                            onValueChange = onOtpChange,
                            label = { Text("کد تایید ۵ رقمی") },
                            leadingIcon = {
                                Icon(Icons.Default.Pin, contentDescription = null, tint = DeepTeal)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            colors = fieldColors,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("otp_code_input")
                        )

                        state.errorMessage?.let { err ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = WarmGold.copy(alpha = 0.5f),
                                border = BorderStroke(1.dp, DeepTeal.copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = err,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = DeepTeal,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                )
                            }
                        }

                        Button(
                            onClick = onVerifyAndEnter,
                            enabled = !state.isSubmitting,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("verify_otp_button"),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepTeal,
                                contentColor = WarmCream
                            )
                        ) {
                            Text(
                                text = "تایید و ورود به بیدارشو",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = onBackToForm,
                                modifier = Modifier.testTag("edit_registration_button")
                            ) {
                                Text("ویرایش اطلاعات", color = DeepTeal)
                            }

                            OutlinedButton(
                                onClick = onResendOtp,
                                enabled = state.resendSeconds == 0,
                                border = BorderStroke(1.dp, SageTeal),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (state.resendSeconds > 0) {
                                        "ارسال مجدد (${state.resendSeconds.toPersianDigits()})"
                                    } else {
                                        "ارسال مجدد کد"
                                    },
                                    color = DeepTeal,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "تمامی اطلاعات شما با حفظ حریم خصوصی در بیدارشو نگهداری می‌شود.",
                style = MaterialTheme.typography.bodySmall,
                color = SageTeal,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
