package dev.veryniche.welcometoflip.util

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import dev.veryniche.welcometoflip.R
import dev.veryniche.welcometoflip.theme.Dimen
import dev.veryniche.welcometoflip.theme.WelcomeToFlipTheme

@Composable
fun AboutAppText(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val annotatedString = buildAnnotatedString {
        append(stringResource(id = R.string.about_instructions))
        pushStringAnnotation(tag = "bgg_wt_link", annotation = stringResource(id = R.string.bgg_link_wttm))
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(stringResource(id = R.string.bgg_link_wt_text))
        }
        append("\n")
        pushStringAnnotation(tag = "bgg_wttm_link", annotation = stringResource(id = R.string.bgg_link_wttm))
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline
            )
        ) {
            append(stringResource(id = R.string.bgg_link_wttm_text))
        }
    }
    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.bodyLarge,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "bgg_wttm_link", start = offset, end = offset)
                .firstOrNull()?.let {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.item))
                    context.startActivity(intent)
                }
        },
        modifier = modifier
    )
}

@Composable
fun ImageCreditText(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val credits = listOf(
        Pair("Rocket by Hasanudin", "https://thenounproject.com/icon/rocket-4925595/"),
        Pair("Plant by Tyler DeHague", "https://thenounproject.com/icon/plant-4982354/"),
        Pair("Robot by Hasanudin", "https://thenounproject.com/icon/robot-4971447/"),
        Pair("Thunder by Siti Masriatun", "https://thenounproject.com/icon/thunder-4978759/"),
        Pair("Remove Schedule by Aris Sunjayay", "https://thenounproject.com/icon/remove-schedule-4359088/"),
        Pair("Astronaut by Akriti Bhusal", "https://thenounproject.com/icon/astronaut-280197/"),
        Pair("Water Drop by Georgiana Ionescu", "https://thenounproject.com/icon/water-drop-1506321/"),
        Pair("Bin by Alice Design", "https://thenounproject.com/icon/bin-2034046/"),
        Pair("Arrow by Jamison Wieser", "https://thenounproject.com/icon/arrow-60381/"),
        Pair("Moon by Rafi Al Hakim", "https://thenounproject.com/icon/moon-6086589/"),
        Pair("Park by Ahmad Faiz", "https://thenounproject.com/icon/park-6721083/"),
        Pair("Fence by kitaicons", "https://thenounproject.com/icon/fence-6826093/"),
        Pair("Mailbox by Fharaz", "https://thenounproject.com/icon/mailbox-6180823/"),
        Pair("Traffic Cone by Dmitry Mirolyubov", "https://thenounproject.com/icon/traffic-cone-57855/"),
        Pair("Pool by Larea", "https://thenounproject.com/icon/pool-5817216/"),
        Pair("Increase by ARISO", "https://thenounproject.com/icon/increase-6785191/"),
        Pair("House by Jang Jeong Eui", "https://thenounproject.com/icon/house-6786764/"),
    )
    val annotatedString = buildAnnotatedString {
        credits.forEachIndexed { index, credit ->
            pushStringAnnotation(
                tag = credit.second,
                annotation = credit.second
            )
            withStyle(
                style = SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(credit.first)
            }
            if (index < credits.size - 1) {
                append(", ")
            }
        }
    }
    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            credits.forEach { credit ->
                annotatedString.getStringAnnotations(
                    tag = credit.second,
                    start = offset,
                    end = offset
                ).firstOrNull()?.let {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.item))
                    context.startActivity(intent)
                }
            }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun ImageCreditTextPreview() {
    WelcomeToFlipTheme {
        ImageCreditText()
    }
}

@Composable
fun RemoveAdsText(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val annotatedString = buildAnnotatedString {
        append(stringResource(id = R.string.welcome_remove_ads_1))
        append(" ")
        pushStringAnnotation(tag = "here", annotation = stringResource(id = R.string.welcome_remove_ads_2))
        withStyle(
            style = SpanStyle(color = MaterialTheme.colorScheme.primary, textDecoration = TextDecoration.Underline)
        ) {
            append(stringResource(id = R.string.welcome_remove_ads_2))
        }
        append(" ")
        append(stringResource(id = R.string.welcome_remove_ads_3))
    }
    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "here", start = offset, end = offset)
                .firstOrNull()?.let {
                    onClick.invoke()
                }
        },
        modifier = modifier
    )
}

@Composable
fun UnorderedListText(textLines: List<Int>, modifier: Modifier = Modifier) {
    val bullet = "\u2022"
    val paragraphStyle = ParagraphStyle(textIndent = TextIndent(restLine = Dimen.bulletTextIndent))
    Text(
        buildAnnotatedString {
            textLines.forEach {
                val string = stringResource(id = it)
                withStyle(style = paragraphStyle) {
                    append(bullet)
                    append("\t")
                    append(string)
                }
            }
        },
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier
    )
}
