package com.example.practicalwork3_variant6.composables.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Перевикористовуваний заголовок екрана: жирний, великий текст
 * з відступом знизу для візуального розділення з рештою вмісту.
 */
@Composable
fun ScreenTitle(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

/**
 * Рядок "підпис — значення", що розтягується на всю ширину контейнера.
 */
@Composable
fun InfoRow(label: String, value: String, valueColor: Color = Color.Unspecified) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontWeight = FontWeight.Medium)
        Text(value, fontWeight = FontWeight.Bold, color = valueColor)
    }
}

/**
 * Кнопка меню на всю ширину — використовується для пунктів головного меню.
 */
@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text)
    }
}

/**
 * Текстове поле форми з підписом, підказкою (supportingText) та
 * відображенням помилки валідації (якщо є) — використовується у
 * Практичному занятті №6 для усіх рівнів складності форм.
 *
 * @param label підпис поля.
 * @param value поточне текстове значення.
 * @param onValueChange обробник зміни тексту.
 * @param hint підказка, що показується, поки немає помилки.
 * @param errorMessage повідомлення про помилку валідації (null, якщо все гаразд).
 */
@Composable
fun ReactorInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    errorMessage: String?
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = errorMessage != null,
        supportingText = {
            Text(errorMessage ?: hint)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}

/**
 * Колір статусу процесу відповідно до теми застосунку — використовується
 * у заняттях 7-8 для реактивного кольорового індикатора стану.
 */
@Composable
fun statusLabelColor(isCritical: Boolean, isWarning: Boolean): Color = when {
    isCritical -> MaterialTheme.colorScheme.error
    isWarning -> Color(0xFFE09F3E)
    else -> Color(0xFF2D6A4F)
}
