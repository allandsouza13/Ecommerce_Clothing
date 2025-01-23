package com.example.ecommerce_clothing

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberAsyncImagePainter
import com.example.ecommerce_clothing.Activity.BaseActivity
import com.example.ecommerce_clothing.Helper.ChangeNumberItemsListener
import com.example.ecommerce_clothing.Helper.ManagmentCart
import com.example.ecommerce_clothing.Model.ItemsModel
import com.example.ecommerce_clothing.R

class CartActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
            CartScreen ( ManagmentCart(this),
            onBackClick={
                finish()

            })

            }
        }
}

fun calculatorCart(managementCart: ManagmentCart, tax: MutableState<Double>) {
    val percentTax = 0.02
    tax.value = Math.round((managementCart.getTotalFee() * percentTax) * 100) / 100.0
}

@Composable
fun CartList(
    cartItems: ArrayList<ItemsModel>,
    managementCart: ManagmentCart,
    onItemChange: () -> Unit
){
    LazyColumn(Modifier.padding(top = 16.dp)) {
        items(cartItems) {item->
            CartItem(
                cartItems = cartItems,
                item = item,
                managementCart = managementCart,
                onItemChange = onItemChange
            )
        }
    }
}

@Composable
fun CartItem(
    cartItems: ArrayList<ItemsModel>,
    item: ItemsModel,
    managementCart: ManagmentCart,
    onItemChange: () -> Unit,
  ) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 8.dp)
    ) {
        val (pic, titleTxt, feeEachTime, totalEachItem, Quantity) = createRefs()

        Image(
            painter = rememberAsyncImagePainter(item.picUrl[0]),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .background(
                    colorResource(R.color.lightBrown),
                    shape = RoundedCornerShape(10.dp)
                )
                .constrainAs(pic){
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }

        )
        Text(
            text = item.title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier
                .constrainAs(titleTxt) {
                    start.linkTo(pic.end, margin = 8.dp)
                    top.linkTo(pic.top)
                }
        )

        Text(
            text = "£${item.price}",
            color = colorResource(R.color.darkBrown),
            fontSize = 14.sp,
            modifier = Modifier
                .constrainAs(feeEachTime) {
                    start.linkTo(titleTxt.start)
                    top.linkTo(titleTxt.bottom, margin = 4.dp)
                }
        )
        Text(
            text = "£${item.numberInCart * item.price}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .constrainAs(totalEachItem) {
                    start.linkTo(titleTxt.start)
                    bottom.linkTo(pic.bottom)
                }
                .padding(start = 8.dp, top = 8.dp)
        )
        ConstraintLayout(
            modifier = Modifier
                .width(100.dp)
                .constrainAs(Quantity) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .background(
                    colorResource(R.color.lightBrown),
                    shape = RoundedCornerShape(100.dp)
                )
        ) {
            val (plusCartBtn, minusCartBtn, numberItemText) = createRefs()
            Text(
                text = item.numberInCart.toString(),
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(numberItemText) {
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            )
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(28.dp)
                    .background(
                        colorResource(R.color.darkBrown),
                        shape = RoundedCornerShape(100.dp)
                    )
                    .constrainAs(plusCartBtn) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        managementCart.plusItem(
                            cartItems,
                            cartItems.indexOf(item),
                            object :ChangeNumberItemsListener {
                                override fun onChanged() {
                                    onItemChange()
                                }
                            }
                        )
                    }
            ) {
                Text(
                    text = "+",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(28.dp)
                    .background(
                        colorResource(R.color.white),
                        shape = RoundedCornerShape(100.dp)
                    )
                    .constrainAs(minusCartBtn) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                    .clickable {
                        managementCart.minusItem(cartItems,
                            cartItems.indexOf(item),
                            object : ChangeNumberItemsListener {
                                override fun onChanged() {
                                    onItemChange()
                                }
                            }
                        )
                    }
            ) {
                Text(
                    text = "-",
                    color = colorResource(R.color.darkBrown),
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CartSummary(itemTotal: Double, tax: Double, delivery: Double) {
    val total=itemTotal + tax + delivery

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)) {
        Text(
            text = "Item Total:",
            Modifier.weight(1f),
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.darkBrown)
        )
            Text(text = "£$itemTotal")

        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)) {
            Text(
                text = "Tax:",
                Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkBrown)
            )
            Text(text = "£$tax")

        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)) {
            Text(
                text = "Delivery:",
                Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkBrown)
            )
            Text(text = "£$delivery")

        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top=16.dp)
        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)) {
            Text(
                text = "Total:",
                Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkBrown)
            )
            Text(text = "£$total",
                fontWeight = FontWeight.Bold, // Added fontWeight here
                color = colorResource(R.color.darkBrown)
            )

        }
        Button(
            onClick = {},
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = colorResource(R.color.darkBrown),
        ),
            modifier = Modifier
                .padding(top=16.dp)
                .fillMaxWidth()
                .height(50.dp)

        ) {
            Text(
                text ="Check Out",
                fontSize = 18.sp,
                color = Color.White
            )
        }
    }

}

@Composable
private fun CartScreen(
    managementCart: ManagmentCart = ManagmentCart(LocalContext.current),
    onBackClick: ()->Unit
) {
    val cartItems = remember { mutableStateOf(managementCart.getListCart()) }
    val tax = remember { mutableStateOf(0.0) }
    calculatorCart(managementCart, tax)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ConstraintLayout(modifier = Modifier.padding(top = 36.dp)) {
            val (backBtn, cartTxt) = createRefs()
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(cartTxt) { centerTo(parent) },
                text = "Your Cart",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp
            )
            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .clickable { onBackClick() }
                    .constrainAs(backBtn) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
            )
        }
        if (cartItems.value.isEmpty()) {
            Text(text = "Cart Is Empty", modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            CartList(cartItems = cartItems.value, managementCart) {
                cartItems.value = managementCart.getListCart()
                calculatorCart(managementCart, tax)
            }
            CartSummary(
                itemTotal = managementCart.getTotalFee(),
                tax = tax.value,
                delivery = 5.0
            )
        }
    }
}
