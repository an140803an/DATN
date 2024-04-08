

app.service("cartService", function ($http, $window) {
  this.message = "";
  this.itemsEdited = [];
  this.items = [];

  this.add = function () {
    var selectedSize = $('input[name="btnradio"]:checked');

    var id = parseInt(selectedSize.attr("id").replace("btnradio", ""));
    var qty = parseInt($("#qty").val());
   
    this.loadLocalStorage();
    // console.log("this.item", this.items);
    // console.log("đã nhận" + id);

    let item = this.items.find((item) => item.productDetailId == id);
    if (item) {
      $http.get("/rest/productDetail/" + id).then((resp) => {
        let qantityTemp = (item.quantity += qty);
        // if (qantityTemp >= resp.data.quantity) {
        //   item.quantity = resp.data.quantity
        //   this.saveLocalStorage();
        //   this.items = [];
        // } else {
        item.quantity = qantityTemp;
        this.saveLocalStorage();
        this.items = [];
        // }
      });
    } else {
      $http.get("/rest/productDetail/" + id).then((resp) => {
        resp.data.quantity = qty;
        this.items.push(resp.data);
        this.saveLocalStorage();
        this.items = [];
      });
    }
  };

  this.remove = function (id) {
    Swal.fire({
      title: "Bạn có chắc chắn muốn xóa sản phẩm này?",
      text: "Thao tác này không thể hoàn tác!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#d33",
      cancelButtonColor: "#3085d6",
      confirmButtonText: "Xóa",
      cancelButtonText: "Hủy",
    }).then((result) => {
      if (result.isConfirmed) {
        let indexItem = this.items.findIndex(
          (item) => item.productDetailId === parseInt(id)
        );
       
        this.items.splice(indexItem, 1);
        this.saveLocalStorage();
        this.loadLocalStorage();
        $window.location.href = "/gioHang";
      //   Swal.fire(
      //     'Đã xóa!',
      //     'Sản phẩm đã được xóa thành công.',
      //     'success'
      // );
      }
     
    });
  };

  this.clear = function () {
    this.items = [];
    this.saveLocalStorage();
  };

  this.getTotalQuantity = function () {
    let json = localStorage.getItem("cart");
    let items = json ? JSON.parse(json) : [];
    // console.log("localStorage", items);
    if (items.length != 0) {
      return items
        .map((item) => item.quantity)
        .reduce((total, qty) => (total += qty), 0);
    } else {
      return 0;
    }
  };

  this.getTotalAmount = function () {
    let json = localStorage.getItem("cart");
    let items = json ? JSON.parse(json) : [];
    if (items.length != 0) {
      return items
        .map(
          (item) =>
            item.quantity *
            (item.product.price -
              item.product.price * (item.product.discount / 100))
        )
        .reduce((total, amt) => (total += amt));
    } else {
      return 0;
    }
  };

  this.loadLocalStorage = function () {
    let json = localStorage.getItem("cart");
    this.items = json ? JSON.parse(json) : [];
  };

  this.saveLocalStorage = function () {
    let json = JSON.stringify(angular.copy(this.items));
    localStorage.setItem("cart", json);
  };

  this.updateQuantity = function (item, numChange) {
    let currentValue = parseInt(item.quantity) || 0;
    let newValue = currentValue + numChange;

    if (newValue < 1) {
      newValue = 1;
      // console.log("newvalue",newValue);
      item.quantity = newValue;
      this.saveLocalStorage();
    } else {
      item.quantity = newValue;

      // console.log("item.quantity ",item.quantity );
      this.saveLocalStorage();
    }
  };

  this.validateInput = function (item) {
    let currentValue = parseInt(item.quantity) || 0;

    if (currentValue < 1) {
      item.quantity = 1;
      this.saveLocalStorage();
    } else {
      item.quantity = currentValue;
      this.saveLocalStorage();
    }
  };

  this.reportNoMoreStock = function (message) {
    // console.log(this.itemsEdited);
    let render = this.itemsEdited.reduce((str, item) => {
      return Object.keys(item).length === 0
        ? str
        : (str += `<br><div>${item.name}: <b>${item.quantityBeforeChange}</b> --> <b>${item.quantityAfterChange}</b> </div>`);
    }, "");

    Swal.fire({
      icon: "error",
      title: "Lỗi!",
      html: `<div>
            <div><b>${message}</b></div> 
            ${render}
            </div>`,
    });
  };

  this.checkout = function () {
    // Tạo mảng promises từ mỗi yêu cầu get

    let promises = this.items.map((item) => {
      return $http
        .get("/rest/productDetail/" + item.productDetailId)
        .then((resq) => {
          let itemEdited = {};
          if (item.quantity > resq.data.quantity && resq.data.quantity == 0) {
            itemEdited.name = item.product.name;
            itemEdited.quantityBeforeChange = item.quantity;
            itemEdited.quantityAfterChange = resq.data.quantity;
            this.remove(item.productDetailId);
          } else if (item.quantity > resq.data.quantity) {
            itemEdited.name = item.product.name;
            itemEdited.quantityBeforeChange = item.quantity;
            itemEdited.quantityAfterChange = resq.data.quantity;
            item.quantity = resq.data.quantity;
            this.saveLocalStorage();
          }
          return itemEdited;
        });
    });

    // Chờ tất cả các promises hoàn thành
    Promise.all(promises).then((itemsEdited) => {
      // itemsEdited sẽ là một mảng các kết quả từ tất cả các promises
      this.itemsEdited = itemsEdited.every(
        (obj) => Object.keys(obj).length === 0
      )
        ? []
        : itemsEdited;
      if (this.itemsEdited.length !== 0) {
        this.reportNoMoreStock(
          "Một số sản phẩm trong giỏ hàng không còn đủ số lượng để đặt hàng. Chúng tôi xin lỗi vì sự bất tiện này."
        );
      } else if(this.items.length === 0){
        Swal.fire({
          icon: "error",
          title: "Lỗi!",
          html: `<div>
                <b>Để thanh toán thành công, vui lòng thêm sản phẩm vào giỏ hàng</b></div> 
               </div>`,
        });
      }else {
        $window.location.href = "/gioHang/checkout";
      }
    });
  };
});

app.controller(
  "shopping-cart-ctrl",
  async function ($scope, cartService, $http, $window) {
    $scope.cart = cartService;

    $scope.cart.loadLocalStorage();

    // Thêm sự kiện click vào button
    const closeButton = document.getElementById("liveAlertPlaceholder");
    if (closeButton) {
      closeButton.addEventListener("click", function () {
        $scope.cart.itemsEdited = [];

        console.log("đã xóa this.message: ", this.message);
      });
    }
    $scope.account = await $http
      .get("/rest/account/currentUser")
      .then((resp) => resp.data);

    $scope.$apply(function () {
      $scope.form = {
        email: $scope.account.email,
        name: $scope.account.name,
        note: "",
        paymentMethodStatusId: "COD",
        phone: $scope.account.phone,
        address: $scope.account.address,
      };
    });

 

    $scope.purchase = async function () {
      let orderData = null;
      orderData = {
        orderId: Math.floor(new Date().getTime() / 10000.0),
        account: $scope.account ? $scope.account : null,
        orderDate: new Date(),
        customerNote: $scope.form.note,
        orderStatus: null,
        paymentMethod: $scope.form.paymentMethodStatusId,
        customerName: $scope.form.name,
        customerPhone: $scope.form.phone,
        customerEmail: $scope.form.email,
        customerAddress: $scope.form.address,
        shippingStatus: "WAITING",
        shippingMoney: 30000,
        paymentStatus: "PENDING",
        orderDetail: $scope.cart.items.map((item) => ({
          productDetailId: item.productDetailId,
          name: item.product.name,
          img: item.product.img,
          price:
            item.product.price -
            (item.product.price * item.product.discount) / 100,
          quantity: item.quantity,
          size: item.size,
        })),
      };
      

      let checkInStock;

      for (let item of $scope.cart.items) {
        try {
          const resq = await $http.get(
            "/rest/productDetail/checkStock/" +
              item.productDetailId +
              "/" +
              item.quantity
          );
        

          if (resq.data === false) {
            checkInStock = resq.data;
          }
        } catch (error) {
          console.error("Error fetching data:", error);
        }
      }

     
      if (checkInStock === false) {
        Swal.fire({
          title:"Lỗi",
          text: "Có sản phẩm trong giỏ hàng không đủ số lượng để đặt hàng (có thể người dùng khác đã đặt hàng trước bạn). Vui lòng thay đổi số lượng.",
          icon: "error",
          confirmButtonText: "OK",
        }).then((result) => {
          if (result.isConfirmed) {
            $window.location.href = "/gioHang";
            
          }
         
        });
        // alert(
        //   "Có sản phẩm trong giỏ hàng không đủ số lượng để đặt hàng. Vui lòng thay đổi số lượng."
        // );
        // window.location.href = "/gioHang";
        // $scope.cart.checkout();
      } else {
        if (orderData.paymentMethod === "COD") {
          let order = await $http
            .post("/rest/order/setOneByJson", JSON.stringify(orderData))
            .then((resp) => {
              return resp.data;
            });
         
          await $http.post(
            "/rest/productDetail/updateByOrderDetailList",
            JSON.stringify(order.orderDetail)
          );

          $scope.cart.clear();

          let amount = order.orderDetail.reduce(
            (total, item) => (total += item.price * item.quantity),
            0
          );
          let startBody = `<p>Xin chào ${order.customerName}.</p> 
            <p>Cảm ơn Anh/Chị đã đặt hàng tại <b>TOKOFASHION</b>.</p>
            <p>Đơn hàng của Anh/Chị đã được tiếp nhận, chúng tôi sẽ nhanh chóng tiến hành giao đơn hàng của Anh/Chị.<p>
            <hr>
            <p><b>Thông tin khách hàng:</b></p>
            ${order.customerName}<br>
			${order.customerPhone}<br>
			${order.customerEmail}<br>
			${order.customerAddress}<br>
			<p><b>Hình thức thanh toán:</b>${order.paymentMethod}</p>
			<p><b>Ngày đặt hàng:</b>${order.orderDate}</p>
            <hr>`;

          let middleBody = order.orderDetail.reduce((str, item) => {
            return (str += `<div>
							<b>${item.name} SIZE ${item.size}</b> 
							<br>
							&nbsp;&nbsp;${item.price} x ${item.quantity} <b style="margin-left: 40px;">${
              item.price * item.quantity
            }</b>
							<div>`);

            // return str += item.name + " SIZE " + item.size + ": " + item.price + " x " + item.quantity + "; "
          }, "");
          let endBody = `<p>Tổng hóa đơn: <b>${amount}</b></p>
		  				<p>Phí vận chuyển: <b>${order.shippingMoney}</b></p>
		  				<p>Thành tiền: <b>${amount + 30000}</b></p>`;
          let fullBody = (startBody += middleBody += endBody);
        

          await $http
            .post(
              `http://localhost:8080/rest/sendmail`,
              JSON.stringify({
                toEmail: order.customerEmail,
                subjectTitle: "TOKOFHASION - Đặt hàng thành công",
                body: fullBody,
              })
            )
            .then(($window.location.href = "/purchase/success"));
        } else if (orderData.paymentMethod === "Online Banking") {
          let CreatePaymentLinkRequestBody = {
            orderCode: orderData.orderId,
            amount: 10000,
            // amount: order.orderDetail.reduce((total, item) => (total += item.price * item.quantity), 0),
            description: orderData.orderId,
            buyerName: orderData.customerName,
            buyerEmail: orderData.customerEmail,
            buyerPhone: orderData.customerPhone,
            buyerAddress: orderData.customerAddress,
            items: orderData.orderDetail.map((item) => {
              return {
                name: item.name + " - SIZE: " + item.size,
                quantity: item.quantity,
                price: item.price,
              };
            }),
            cancelUrl: "http://localhost:8080/cancelURL",
            returnUrl: "http://localhost:8080/returnURL",
            expiredAt: parseInt((new Date().getTime() + 5 * 60 * 1000) / 1000),
            signature: "",
          };
        
          let paymentOnlineLinkData = await $http
            .post(
              "/rest/payos/createPaymentOnlineLink",
              JSON.stringify(CreatePaymentLinkRequestBody)
            )
            .then((resp) => {
             
              return resp.data;
            });
          // window.location.href = resp.data.data.checkoutUrl
          if (paymentOnlineLinkData.code === "00") {
            let order = await $http
              .post("/rest/order/setOneByJson", JSON.stringify(orderData))
              .then((resp) => resp.data);

            await $http.post(
              "/rest/productDetail/updateByOrderDetailList",
              JSON.stringify(order.orderDetail)
            );

            $scope.cart.clear();

            window.location.href = paymentOnlineLinkData.data.checkoutUrl;
          } else {
            alert("Thanh toán không thành công xin vui lòng thử lại");
          }
        }

        //window.location.href = '/purchase/successful';
      }
    };
  }
);
