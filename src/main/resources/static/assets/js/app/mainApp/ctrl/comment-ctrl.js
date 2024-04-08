app.controller("comment-ctrl", function ($scope,$location,$http) {

  var currentUrl = $location.absUrl();
  // Sử dụng các phương thức xử lý chuỗi để lấy số 1 từ URL
  var productId = extractProductIdFromUrl(currentUrl);

  // Các phương thức xử lý chuỗi có thể được thực hiện dựa trên cấu trúc URL cụ thể của bạn
  function extractProductIdFromUrl(url) {
    // Ví dụ: Giả sử URL có cấu trúc như "http://localhost:8080/product/detail/1"
    var parts = url.split('/');
    var productIdIndex = parts.indexOf('product') + 2; // Lấy chỉ số của '1' trong URL
    var productId = parts[productIdIndex];
    return productId;
  }

  $scope.getComment = function () {
    $http.get("http://localhost:8080/rest/productDetail/getComment/" + productId)
      .then(function (response) {
        $scope.listComment = response.data;
       

      })
      .catch(function (error) {
        console.log(error);
      });
  }
  $scope.getComment();

  $scope.addComment = function () {
    var formData = new FormData();
    formData.append('content', $scope.newComment);

    $http.post("http://localhost:8080/rest/productDetail/addComment/" + productId, formData, {
      transformRequest: angular.identity,
      headers: {
        'Content-Type': undefined
      }
    })
      .then(function (response) {
        alert("Đã thêm thành công")
        $scope.getComment();
      })
      .catch(function (error) {
        console.log(error);
      });
  }

  //định dạng thời gian
  $scope.getFormattedTimeAgo = function (date) {
    var currentTime = new Date();
    var activityTime = new Date(date);
    var timeDiff = currentTime.getTime() - activityTime.getTime();
    var seconds = Math.floor(timeDiff / 1000);
    var minutes = Math.floor(timeDiff / (1000 * 60));
    var hours = Math.floor(timeDiff / (1000 * 60 * 60));
    var days = Math.floor(timeDiff / (1000 * 60 * 60 * 24));

    if (days === 0) {
      if (hours === 0 && minutes < 60) {
        if (seconds < 60) {
          return 'vài giây trước';
        } else {
          return minutes + ' phút trước';
        }
      } else if (hours < 24) {
        return hours + ' giờ trước';
      }
    } else if (days === 1) {
      return 'Hôm qua';
    } else if (days <= 7) {
      return days + ' ngày trước';
    } else {
      // Hiển thị ngày, tháng và năm của activityTime
      var formattedDate = activityTime.getDate();
      var formattedMonth = activityTime.getMonth() + 1; // Tháng trong JavaScript đếm từ 0, nên cần cộng thêm 1
      var formattedYear = activityTime.getFullYear();
      return formattedDate + '-' + formattedMonth + '-' + formattedYear;
    }
  };
})