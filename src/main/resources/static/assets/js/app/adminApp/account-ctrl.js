
app.controller('account-ctrl', function ($scope, $http, $window, $rootScope, $location, $timeout) {
    $scope.listUser = [];
    $scope.account = {};
    $scope.newAccount = {
        "account_id": "",
        "name": null,
        "email": null,
        "phone": null,
        "img": null,
        "password": null,
        "gender": null,
        "address": null,
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
        { value: "Ad", label: "Admin" },
        { value: "Cust", label: "Customer" },
        // Các vai trò khác nếu có
    ];

    var url = "http://localhost:8080/rest/account";
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
        var phoneRegex = /^0\d{9}$/;
        if (phoneRegex.test($scope.account.phone)) {
        } else {
            alert('Số điện thoại không hợp lệ');
            return;
        }

        if ($scope.account.authorities[0].role.id === "Ad") {
            alert("Không được đổi thành vai trò admin");
            return;
        }

        if ($scope.account.authorities[0].role.id === "Cust") {
            $scope.account.authorities[0].role.name = "Customers";
        } else if ($scope.account.authorities[0].role.id === "Ad") {
            $scope.account.authorities[0].role.name = "Admin";
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
        if ($scope.newAccount.password.length <= 5) {
            alert("Mật khẩu phải trên 6 ký tự");
            return;
        }
        var phoneRegex = /^0\d{9}$/;
        if (phoneRegex.test($scope.newAccount.phone)) {
        } else {
            alert('Số điện thoại không hợp lệ');
            return;
        }

        for (var i = 0; i < $scope.listUser.length; i++) {
            if ($scope.listUser[i].phone === $scope.newAccount.phone) {
                alert('Số điện thoại trùng lặp, vui lòng đổi số điện thoại khác!');
                return;
            }
        }

        for (var i = 0; i < $scope.listUser.length; i++) {
            if ($scope.listUser[i].email === $scope.newAccount.email) {
                alert('Email trùng lặp, vui lòng đổi email khác!');
                return;
            }
        }

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
                $scope.getListAccount();
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


