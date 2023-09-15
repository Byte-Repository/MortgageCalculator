package com.example.mortgagecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mortgagecalculator.ui.theme.MortgageCalculatorTheme
import java.text.NumberFormat
import kotlin.math.*


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MortgageCalculatorTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MortgageCalculatorLayout()
                }
            }
        }
    }
}

@Composable
fun MortgageCalculatorLayout() {
    var loanAmountInput by remember { mutableStateOf("") }
    var interestInput by remember { mutableStateOf("") }
    var numberOfYearsInput by remember { mutableStateOf("") }

    // Convert input values to the required types
    val loanAmount = loanAmountInput.toIntOrNull() ?: 0
    val annualInterestRate = (interestInput.toDoubleOrNull() ?: 0.0) / 100.0
    val numberOfYears = numberOfYearsInput.toIntOrNull() ?: 0
    val numberOfMonths = numberOfYears * 12

    // Calculate the mortgage using the provided function
    val mortgageResult = calculateMortgage(loanAmount, annualInterestRate, numberOfMonths)

    Column(
        modifier = Modifier
            .padding(40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.mortgage_calculator),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            label = R.string.loan_amount,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = loanAmountInput,
            onValueChange = { loanAmountInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        EditNumberField(
            label = R.string.interest_rate,
            leadingIcon = R.drawable.percent,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = interestInput,
            onValueChange = { interestInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        EditNumberField(
            label = R.string.number_of_years,
            leadingIcon = R.drawable.calendar,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = numberOfYearsInput,
            onValueChange = { numberOfYearsInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.monthly_payment, mortgageResult),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(150.dp))

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null)},
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}

private fun calculateMortgage(p: Int, r: Double, n: Int): String {
    val monthlyInterestRate = r / 12
    val mortgage = p * (monthlyInterestRate * (1 + monthlyInterestRate).pow(n)) /
            ((1 + monthlyInterestRate).pow(n) - 1)

    return if (mortgage.isNaN() || mortgage.isInfinite()) {
        "$0.00"
    } else {
        NumberFormat.getCurrencyInstance().format(mortgage)
    }
}

@Preview(showBackground = true)
@Composable
fun MortgageCalculatorPreview() {
    MortgageCalculatorTheme {
        MortgageCalculatorLayout()
    }
}


