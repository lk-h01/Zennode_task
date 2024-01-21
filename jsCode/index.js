"use strict";

const itemA = {
  name: "ProductA",
  quantity: 0,
  isGift: false,
  price: 20,
};

const itemB = {
  name: "ProductB",
  quantity: 0,
  isGift: false,
  price: 40,
};

const itemC = {
  name: "ProductC",
  quantity: 0,
  isGift: false,
  price: 50,
};

const itemList = [itemA, itemB, itemC];

let prdAQty, prdBQty, prdCQty;

const submitBtn = document.getElementById("mySubmit");

submitBtn.addEventListener("click", function (e) {
  e.preventDefault();
  prdAQty = document.getElementById("prdAQty").value;
  prdBQty = document.getElementById("prdBQty").value;
  prdCQty = document.getElementById("prdCQty").value;
  if (prdAQty != "") itemA.quantity = prdAQty;
  if (prdBQty != "") itemB.quantity = prdBQty;
  if (prdCQty != "") itemC.quantity = prdCQty;
  itemA.isGift = document.getElementById("prdAGift").checked;
  itemB.isGift = document.getElementById("prdBGift").checked;
  itemC.isGift = document.getElementById("prdCGift").checked;

  const totalCartPrice = getTotalPrice();
  const totalQuantity = getTotalQuantity();
  getDiscount(totalCartPrice, totalQuantity);
  console.log(totalCartPrice);

  console.log(coupon);
  const giftWrapAmt = getGiftWrap();
  console.log(giftWrapAmt);
  const shipmentAmt = getShipmentAmt(totalQuantity);
  console.log(shipmentAmt);
  const finalAmt =
    totalCartPrice - coupon.discountValue + giftWrapAmt + shipmentAmt;
  console.log(finalAmt);

  document.getElementById("totalAmt").innerHTML = `$${totalCartPrice}`;
  document.getElementById(
    "coupon"
  ).innerHTML = `Name: ${coupon.name} </br>  Value: $${coupon.discountValue}`;
  document.getElementById("giftAmt").innerHTML = `$${giftWrapAmt}`;
  document.getElementById("shipmentAmt").innerHTML = `$${shipmentAmt}`;
  document.getElementById("finalAmt").innerHTML = `$${finalAmt}`;

  resetItemValues();
});

function getGiftWrap() {
  let sum = 0;
  itemList.forEach((item) => {
    if (item.isGift) {
      sum = sum + 1 * item.quantity;
    }
  });
  return sum;
}

function getShipmentAmt(totalQuantity) {
  return (
    5 *
    (totalQuantity % 10 === 0
      ? Math.floor(totalQuantity / 10)
      : Math.floor(totalQuantity / 10) + 1)
  );
}

function getTotalPrice() {
  let total = 0;
  itemList.forEach((item) => {
    total = total + item.quantity * item.price;
  });
  return total;
}

function getTotalQuantity() {
  let sum = 0;
  itemList.forEach((item) => {
    sum = sum + Number(item.quantity);
  });
  return sum;
}

const coupon = {
  name: "",
  discountValue: 0,
};

function resetItemValues() {
  itemList.forEach((item) => {
    item.isGift = false;
    item.quantity = 0;
  });
  coupon.name = "";
  coupon.discountValue = 0;
}

function getDiscount(totalCartPrice, totalQuantity) {
  const couponToDiscount = new Map();
  let discount = 0;
  if (totalQuantity > 30) {
    itemList.forEach((item) => {
      if (item.quantity > 15) {
        const noOFQtyForDis = item.quantity - 15;
        const price = noOFQtyForDis * item.price;
        discount = discount + price * 0.5;
      }
    });
    console.log(
      "By applying the Coupon 'tiered_50_discount' you get a discount of " +
        discount
    );
  }
  couponToDiscount.set("tiered_50_discount", discount);

  discount = 0;
  if (totalQuantity > 20) {
    discount = totalCartPrice * 0.1;
    console.log(
      "By applying the Coupon 'bulk_10_discount' you get a discount of " +
        discount
    );
  }
  couponToDiscount.set("bulk_10_discount", discount);

  discount = 0;
  if (totalCartPrice > 200) {
    discount = 10;
    console.log(
      "By applying the Coupon 'flat_10_discount' you get a discount of " +
        discount
    );
  }
  couponToDiscount.set("flat_10_discount", discount);

  discount = 0;
  itemList.forEach((item) => {
    if (item.quantity > 10) {
      discount = discount + item.quantity * item.price * 0.05;
      console.log(
        "By applying the Coupon 'bulk_5_discount' you get a discount of " +
          discount
      );
    }
  });
  couponToDiscount.set("bulk_5_discount", discount);

  couponToDiscount.forEach(function (value, key) {
    if (value > coupon.discountValue) {
      coupon.discountValue = value;
      coupon.name = key;
    }
  });
}
