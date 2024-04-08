var app = angular.module("myApp", [])
app.controller('myCtrl', function ($scope, $http, $window, $rootScope, $location, $timeout) {
    $scope.listUser = [];
    $scope.account = {};
    $scope.newAccount = {
        "account_id": "",
        "name": null,
        "email": null,
        "phone": null,
        "img": null,
        "username": null,
        "password": null,
        "gender": null,
        "address": null,
        "country": null,
        "createDate": null,
        "authorities": [
            {
                "authorities_id": 0,
                "role": {
                    "name": null,
                    "id": null
                }
            }
        ]
    };

    $scope.roles = [
        { value: "Staf", label: "Staffs" },
        { value: "Dire", label: "Director" },
        { value: "Cust", label: "Customer" },
        // Các vai trò khác nếu có
    ];

    var url = "http://localhost:8080";

    var config = {
        apiKey: "AIzaSyCTvt2IypHX_Ap3XeBUJkm6ZgojbYyCWio",
        authDomain: "datnv2-e60ce.firebaseapp.com",
        projectId: "datnv2-e60ce",
        storageBucket: "datnv2-e60ce.appspot.com",
        messagingSenderId: "115839709060",
        appId: "1:115839709060:web:5dc7418d3006d52ddbf7d1",
        measurementId: "G-BLV14MN29Y"
    };

    // Kiểm tra xem Firebase đã được khởi tạo chưa trước khi khởi tạo nó
    if (!firebase.apps.length) {
        firebase.initializeApp(config);
    }

    $scope.getListAccount = function () {
        $http.get(url + '/get-list-account')
            .then(function (response) {
                $scope.listUser = response.data;
            }, function (error) {
                // Xử lý lỗi
                console.log(error);
            });
    }
    $scope.getListAccount();

    $scope.deleteAccount = function (accountId) {
        $http.get(url + '/delete-account/' + accountId)
            .then(function (response) {
                $scope.listUser = response.data;
            }, function (error) {
                // Xử lý lỗi
                console.log(error);
            });
    }

    $scope.findAccount = function (accountId) {
        $http.post(url + '/find-account/' + accountId)
            .then(function (response) {
                $scope.account = response.data;
            }, function (error) {
                // Xử lý lỗi
                console.log(error);
            });
    }

    $scope.saveAccount = function () {
        if ($scope.account.authorities[0].role.id === "Cust") {
            $scope.account.authorities[0].role.name = "Customers";
        } else if ($scope.account.authorities[0].role.id === "Dire") {
            $scope.account.authorities[0].role.name = "Directors";
        }
        else {
            $scope.account.authorities[0].role.name = "Staffs";
        }
        console.log("$scope.account", $scope.account)
        $http.post(url + '/save-account', $scope.account)
            .then(function (response) {
                $scope.getListAccount();
                alert("Cập nhật tài khoản thành công!!!")
            }, function (error) {
                // Xử lý lỗi
                console.log(error);
            });

    }
    $scope.addAccount = function () {
        $scope.newAccount.create_date = null;
        var fileInput = document.getElementById('inputGroupFile01');
        var selectedFile = fileInput.files[0];  // Lấy file đầu tiên trong danh sách

        if (selectedFile) {
            console.log("Tên ảnh đã chọn:", selectedFile.name);
        } else {
            console.log("Không có file nào được chọn");
        }
        $scope.newAccount.img = selectedFile.name;
        console.log("$scope.newAccount", $scope.newAccount)

        $http.post(url + '/add-account', $scope.newAccount)
            .then(function (response) {
                $scope.saveImage();
                alert("Thêm thành công");
                $scope.getListAccount();
            }, function (error) {
                alert("Vui lòng nhập đủ thông tin");
            });
    }
    $scope.saveImage = function () {
        var formData = new FormData();
        var fileInput = document.getElementById('inputGroupFile01');
        for (var i = 0; i < fileInput.files.length; i++) {
            var file = fileInput.files[i];
            var fileSizeMB = file.size / (1024 * 1024); // Kích thước tệp tin tính bằng megabyte (MB)
            if (fileSizeMB > 1000) {
                const Toast = Swal.mixin({
                    toast: true,
                    position: 'top-end',
                    showConfirmButton: false,
                    timer: 3000,
                    timerProgressBar: true,
                    didOpen: (toast) => {
                        toast.addEventListener('mouseenter', Swal.stopTimer)
                        toast.addEventListener('mouseleave', Swal.resumeTimer)
                    }
                });

                Toast.fire({
                    icon: 'warning',
                    title: 'Kích thước tệp tin quá lớn (giới hạn 1GB)'
                });

                return; // Return without doing anything
            }
            for (var i = 0; i < fileInput.files.length; i++) {
                formData.append('photoFiles', fileInput.files[i]);
            }

            $http.post(url + '/save-image', formData, {
                transformRequest: angular.identity,
                headers: {
                    'Content-Type': undefined
                }
            }).then(function (response) {
                alert("ok")
            }, function (error) {
                // Xử lý lỗi
                console.log(error);
            })


        }
    }

})


