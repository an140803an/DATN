var app = angular.module("adminApp", []);

app.filter("formatCurrency", [
    "$filter",
    function ($filter) {
      return function (input) {
        return $filter("number")(input, 0).replace(/,/g, ".") + " VND";
      };
    },
  ]);
  
app.controller("product-ctrl", function ($scope, $http) {
    var url = 'http://localhost:8080/rest/products';
    var categoryUrl = 'http://localhost:8080/rest/categories';
    var produDetailUrl = 'http://localhost:8080/rest/productDetail/detail';
    var updateQuantityUrl = 'http://localhost:8080/rest/productDetail/quantity';
    var createSizeUrl = 'http://localhost:8080/rest/productDetail/size';
    var imgUrl = 'http://localhost:8080/rest/products/upload';
    $scope.form = {}
    $scope.list = []
    $scope.categories = []


    $scope.formProductDetail = {}

    $scope.formUpdateQuantity = {}

    $scope.searchQuery = '';
    $scope.searchResults = [];


    $scope.uploadFile = function () {
        var file = $scope.form.img;
        var formData = new FormData();
        formData.append('file', file);

        $http.post('/rest/products/upload', formData, {
            transformRequest: angular.identity,
            headers: { 'Content-Type': undefined }
        }).then(function (response) {
           
            alert('File uploaded successfully!');
        }, function (error) {
            console.error('Error uploading file:', error.data);
            alert('Failed to upload file');
        });
    };




    // // Hàm tìm kiếm sản phẩm theo ID
    // $scope.searchProductById = function (id) {
    //     $http.get(`${url}/${id}`)
    //         .then(function (response) {
    //             $scope.list = [response.data];
    //         })
    //         .catch(function (error) {
    //             console.error("Error searching product by ID:", error);
    //         });
    // };



    // Phân trang

    $scope.begin = 0;
    $scope.pageSize = 8;
    $scope.pageCount = 0;
    // Hàm cập nhật số trang
    function updatePageCount() {
        $scope.pageCount = Math.ceil($scope.list.length / $scope.pageSize);
    }

    // Hàm load danh sách sản phẩm từ API
    function loadProducts() {
        $http.get(url)
            .then(function (response) {
                $scope.list = response.data;
                updatePageCount(); // Cập nhật pageCount sau khi sản phẩm được tải
            })
            .catch(function (error) {
                console.error("Lỗi khi tải danh sách sản phẩm:", error);
            });
    }

    // Load sản phẩm khi controller khởi tạo
    loadProducts();

    $scope.first = function () {
        $scope.begin = 0;
    };

    $scope.prev = function () {
        if ($scope.begin > 0) {
            $scope.begin -= $scope.pageSize;
        }
    };

    $scope.next = function () {
        if ($scope.begin < ($scope.pageCount - 1) * $scope.pageSize) {
            $scope.begin += $scope.pageSize;
        }
    };

    $scope.last = function () {
        $scope.begin = ($scope.pageCount - 1) * $scope.pageSize;
    };


    $scope.getPages = function () {
        const currentPage = Math.floor($scope.begin / $scope.pageSize) + 1;
        const totalPages = $scope.pageCount;

        let startPage, endPage;

        // Hiển thị 5 trang cố định
        if (currentPage <= 3) {
            startPage = 1;
            endPage = Math.min(totalPages, 5);
        } else {
            startPage = currentPage - 2;
            endPage = Math.min(totalPages, currentPage + 2);
        }

        return new Array(endPage - startPage + 1).fill().map((_, index) => startPage + index);
    };

    $scope.goToPage = function (page) {
        // Kiểm tra xem đang ở chế độ tìm kiếm không
        if ($scope.isSearching) {
            // Nếu đang ở chế độ tìm kiếm, thực hiện tìm kiếm lại trên trang mới
            $scope.searchById();
        } else {
            // Nếu không ở chế độ tìm kiếm, thực hiện chuyển trang bình thường
            $scope.begin = page * $scope.pageSize;
            // Load dữ liệu mới từ server tại đây
            $scope.load();
        }
    };


    $scope.searchId = '';

    // Hàm tìm kiếm theo ID sản phẩm
    $scope.searchById = function () {
        // Đặt biến isSearching để thông báo rằng đang ở chế độ tìm kiếm
        $scope.isSearching = true;

        // Kiểm tra xem $scope.searchId có giá trị không
        if ($scope.searchId) {
            // Gọi API hoặc thực hiện logic tìm kiếm ở đây với $scope.searchId
            $http.get(url + '/' + $scope.searchId)
                .then(function (response) {
                    // Xử lý kết quả tìm kiếm ở đây
                   
                    $scope.reset();
                    $scope.list = [response.data];
                })
                .catch(function (error) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Không tìm thấy',
                        text: 'Không tìm thấy sản phẩm có id mà bạn vừa nhập.',
                    });
                    console.error("Error searching product by ID:", error);
                })
                .finally(function () {
                    // Reset biến isSearching sau khi tìm kiếm hoàn tất
                    $scope.isSearching = false;
                });
        } else {
            // Hiển thị thông báo hoặc thực hiện hành động phù hợp khi không có giá trị ID
            console.log("Please enter a valid ID for search.");
        }
    };
    //  // Hàm tìm kiếm theo categoryId
    //  $scope.searchByCategoryId = function (id) {
    //     $http.get(`${url}/searchByCategoryId/${id}`)
    //         .then(function (response) {
    //             // Xử lý kết quả tìm kiếm ở đây
    //             console.log(response.data);
    //             $scope.list = response.data;
    //         })
    //         .catch(function (error) {
    //             console.error("Error searching products by categoryId:", error);
    //         });
    // };

    // Hàm khi nhấn nút "Cancel"
    $scope.cancelSearch = function () {
        $scope.searchId = ''; // Xóa giá trị trong ô input tìm kiếm
        $scope.load(); // Đặt lại danh sách sản phẩm về trạng thái ban đầu
    };





    // $scope.quantity;
    $scope.updateQuantity = function () {
        // Gọi hàm getSelectedQuantity để cập nhật giá trị cho input quantity
        $scope.quantity = $scope.getSelectedQuantity();
    };





    $scope.getSelectedQuantity = function () {
        if ($scope.selectedSize) {
            // Tìm productDetail có size tương ứng
            var selectedProductDetail = $scope.form.productDetail.find(function (pd) {
                return pd.productDetailId === $scope.selectedSize;
            });

            // Trả về quantity của productDetail tương ứng
            return selectedProductDetail ? selectedProductDetail.quantity : 0;
        }

        return 0;
    };

    $scope.load = function () {
        $http.get(url).then(resp => {
           
            $scope.list = resp.data;
        });
        $http.get(categoryUrl).then(resp => {
            $scope.categories = resp.data;
        });

    }
    $scope.edit = function (id) {
        $http.get(`${url}/${id}`).then(resp => {
            
            $scope.form.product = resp.data;
        });
        $http.get(`${produDetailUrl}/${id}`).then(resp => {
            
            $scope.form.productDetail = resp.data;
        });
       
    }
    $scope.reset = function () {
        $scope.form = {}
    }

    // $scope.create = function() {
    //     var data = angular.copy($scope.form);
    //     $http.post(url, data).then(resp => {
    //         $scope.reset();
    //         $scope.load();
    //     });
    // }

    // $scope.create = function () {
    //     var data = angular.copy($scope.form);
    //     if (data.name == null) {
    //         console.log("lỗi ẻ")
    //     } else {
    //         $http.post(url, data).then(resp => {
    //             // Thông báo thành công
    //             Swal.fire({
    //                 icon: 'success',
    //                 title: 'Tạo thành công!',
    //                 showConfirmButton: false,
    //                 timer: 1500  // Tự động đóng sau 1.5 giây
    //             });

    //             $scope.reset();
    //             $scope.load();
    //         }).catch(error => {
    //             // Thông báo lỗi
    //             Swal.fire({
    //                 icon: 'error',
    //                 title: 'Lỗi khi tạo!',
    //                 text: 'Đã xảy ra lỗi khi thực hiện tạo.',
    //             });
    //         });
    //     }

    // };

    $scope.create = function () {
        if ($scope.form && $scope.form.name !== undefined && $scope.form.price !== undefined) {
            var data = angular.copy($scope.form);

            // Kiểm tra xem đã chọn tệp tin chưa

            const cateSelect = document.getElementById('cateSelect');
            var text = cateSelect.options[cateSelect.selectedIndex].text;
            if (text === "") {
            } else {

                $http.post(url, data).then(resp => {
                    // Thông báo thành công
                    Swal.fire({
                        icon: 'success',
                        title: 'Tạo thành công!',
                        showConfirmButton: false,
                        timer: 1500  // Tự động đóng sau 1.5 giây
                    });

                    $scope.reset();
                    $scope.load();
                }).catch(error => {
                    // Thông báo lỗi
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi khi tạo!',
                        text: 'Đã xảy ra lỗi khi thực hiện tạo.',
                    });
                });
            }
        } else {
        }
    };


    // $scope.create = function () {
    //     if ($scope.form && $scope.form.name !== undefined && $scope.form.price !== undefined) {
    //         var data = angular.copy($scope.form);
    //         const fileInput = document.getElementById('chooseFile');
    //         // Kiểm tra xem đã chọn tệp tin chưa
    //         if (fileInput.files.length > 0) {
    //             // Lấy tên của tệp tin đã chọn
    //             var fileName = fileInput.files[0].name;
    //             if (fileName === 'balone.webp') {
    //                 data.img = 'https://bizweb.dktcdn.net/thumb/large/100/415/697/products/1-c3eb1db4-6cc5-4f09-820d-cdad25250a17.jpg?v=1701402473953'
    //                 $http.post(url, data).then(resp => {
    //                 // Thông báo thành công
    //                 Swal.fire({
    //                     icon: 'success',
    //                     title: 'Tạo thành công!',
    //                     showConfirmButton: false,
    //                     timer: 1500  // Tự động đóng sau 1.5 giây
    //                 });

    //                 $scope.reset();
    //                 $scope.load();
    //             }).catch(error => {
    //                 // Thông báo lỗi
    //                 Swal.fire({
    //                     icon: 'error',
    //                     title: 'Lỗi khi tạo!',
    //                     text: 'Đã xảy ra lỗi khi thực hiện tạo.',
    //                 });
    //             });
    //             }
    //            else if (fileName === 'img1.webp') {
    //                 data.img = 'https://bizweb.dktcdn.net/thumb/large/100/415/697/products/2-66e9bfbf-d1f6-41bf-8930-940af74de9a9.jpg?v=1701860108030'
    //                 $http.post(url, data).then(resp => {
    //                 // Thông báo thành công
    //                 Swal.fire({
    //                     icon: 'success',
    //                     title: 'Tạo thành công!',
    //                     showConfirmButton: false,
    //                     timer: 1500  // Tự động đóng sau 1.5 giây
    //                 });

    //                 $scope.reset();
    //                 $scope.load();
    //             }).catch(error => {
    //                 // Thông báo lỗi
    //                 Swal.fire({
    //                     icon: 'error',
    //                     title: 'Lỗi khi tạo!',
    //                     text: 'Đã xảy ra lỗi khi thực hiện tạo.',
    //                 });
    //             });
    //             }
    //             // In ra tên của tệp tin

    //             // Tiếp tục với logic xử lý khác (nếu cần)
    //         } else {
    //             // Thông báo nếu không có tệp tin nào được chọn
    //             console.log("Vui lòng chọn một tệp tin.");
    //         }

    //         const cateSelect = document.getElementById('cateSelect');
    //         var text = cateSelect.options[cateSelect.selectedIndex].text;
    //         if (text === "") {
    //         } else {
    //             // $http.post(url, data).then(resp => {
    //             //     // Thông báo thành công
    //             //     Swal.fire({
    //             //         icon: 'success',
    //             //         title: 'Tạo thành công!',
    //             //         showConfirmButton: false,
    //             //         timer: 1500  // Tự động đóng sau 1.5 giây
    //             //     });

    //             //     $scope.reset();
    //             //     $scope.load();
    //             // }).catch(error => {
    //             //     // Thông báo lỗi
    //             //     Swal.fire({
    //             //         icon: 'error',
    //             //         title: 'Lỗi khi tạo!',
    //             //         text: 'Đã xảy ra lỗi khi thực hiện tạo.',
    //             //     });
    //             // });
    //         }
    //     } else {
    //     }
    // };








    $scope.update = async function () {
        if (!$scope.form.product.name || !$scope.form.product.price) {
            Swal.fire({
                icon: 'error',
                title: 'Lỗi!',
                text: 'Vui lòng nhập đầy đủ thông tin.',
            });
            return; // Dừng lại nếu tên hoặc giá không được nhập
        }
        var data = angular.copy($scope.form.product);

        await $http.put(`${url}/${data.productId}`, data)
            .then(resp => {
                // Kiểm tra xem có dữ liệu trả về không
                if (resp.data) {
                    $scope.load();
                    // Hiển thị thông báo thành công
                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công!',
                        text: 'Thông tin sản phẩm đã được cập nhật thành công.',
                    });
                } else {
                    // Không có dữ liệu trả về, không hiển thị thông báo
                    // Có thể thực hiện các hành động khác nếu cần thiết
                }
            })
            .catch(error => {
                alert("Không tìm thấy loại hàng để cập nhật");
            });

        let data2 = {
            productDetailId: angular.copy($scope.selectedSize),
            quantity: angular.copy($scope.quantity)
        };

        // Kiểm tra nếu số lượng không được nhập, thì bỏ qua
        if (data2.quantity !== undefined && data2.quantity !== null) {
            await $http.put(`${updateQuantityUrl}/${data2.productDetailId}/${data2.quantity}`, data2)
                .then(resp => {
                    $scope.load();
                    // Hiển thị thông báo thành công
                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công!',
                        text: 'Số lượng sản phẩm đã được cập nhật thành công.'
                    });
                })
                .catch(error => {
                    // alert("Không tìm thấy số lượng cập nhật");
                });
        }

        let data3 = {
            newSize: angular.copy($scope.newSize),
            newQuantity: angular.copy($scope.newQuantity)
        };

        // Kiểm tra nếu newQuantity không được nhập, thì bỏ qua
        if (data3.newSize !== undefined || data3.newQuantity !== undefined) {
            await $http.post(`${createSizeUrl}/${data.productId}/${data3.newSize}/${data3.newQuantity}`)
                .then(resp => {
                    $scope.load();

                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công!',
                        text: 'Size và số lượng đã được thêm thành công.'
                    });
                    $scope.newSize = '';
                    $scope.newQuantity = '';
                })
                .catch(error => {

                });
        }
    };





    $scope.delete = function (id) {
        // Hiển thị cửa sổ xác nhận xóa
        Swal.fire({
            title: 'Xác nhận xóa?',
            text: 'Bạn có chắc muốn xóa sản phẩm này?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Xóa',
            cancelButtonText: 'Hủy bỏ'
        }).then((result) => {
            if (result.isConfirmed) {
                // Nếu người dùng xác nhận xóa
                $http.put(`${url}/${id}/disable`).then(resp => {
                    // Cập nhật trực tiếp trạng thái sản phẩm trong mảng list
                   
                    var index = $scope.list.findIndex(item => item.productId === id);
                    if (index !== -1) {
                        // Cập nhật trạng thái sản phẩm
                        $scope.list[index].descStatus = "The product is out of stock";


                    }
                })
            }
            // Sử dụng $scope.$apply để cập nhật giao diện người dùng
            $scope.$apply(() => {
                // Load lại trang
                // Hiển thị thông báo xóa thành công
                Swal.fire({
                    icon: 'success',
                    title: 'Xóa thành công!',
                    showConfirmButton: false,
                    timer: 1500  // Tự động đóng sau 1.5 giây
                });
                $scope.load();
            });
        });
    };



    $scope.init = function () { }

    $scope.load();
});

app.controller("category-ctrl", function ($scope, $http) {
    var url = 'http://localhost:8080/rest/categories';

    $scope.form = {}
    $scope.list = []

    $scope.load = function () {
        $http.get(url).then(resp => {
           
            $scope.list = resp.data;
        });
    }
    $scope.edit = function (id) {
        $http.get(`${url}/${id}`).then(resp => {
           
            $scope.form = resp.data;
        });
    }
    $scope.reset = function () {
        $scope.form = {}
    }
    // $scope.create = function () {
    //     var data = angular.copy($scope.form);
    //     $http.post(url, data).then(resp => {
    //         $scope.reset();
    //         $scope.load();
    //         Swal.fire({
    //             icon: 'success',
    //             title: 'Thành công!',
    //             text: 'Danh mục đã được tạo thành công.',
    //         });
    //     }).catch(error => {
    //         Swal.fire({
    //             icon: 'error',
    //             title: 'Lỗi!',
    //             text: 'Không thể tạo danh mục. Vui lòng thử lại.',
    //         });
    //     });
    // }

    $scope.create = function () {
        if ($scope.form && $scope.form.type !== undefined) {
            var data = angular.copy($scope.form);

            // Kiểm tra xem đã chọn tệp tin chưa
                $http.post(url, data).then(resp => {
                    // Thông báo thành công
                    Swal.fire({
                        icon: 'success',
                        title: 'Tạo thành công!',
                        showConfirmButton: false,
                        timer: 1500  // Tự động đóng sau 1.5 giây
                    });

                    $scope.reset();
                    $scope.load();
                }).catch(error => {
                    // Thông báo lỗi
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi khi tạo!',
                        text: 'Đã xảy ra lỗi khi thực hiện tạo.',
                    });
                });
            
        } else {
        }
    };


    $scope.update = function () {
       // Kiểm tra xem trường tên có được nhập hay không
    if (!$scope.form.type) {
        Swal.fire({
            icon: 'error',
            title: 'Lỗi!',
            text: 'Vui lòng nhập tên trước khi cập nhật danh mục.',
        });
        return; // Dừng lại nếu tên không được nhập
    }
            var data = angular.copy($scope.form);
            $http.put(`${url}/${data.categoryId}`, data).then(resp => {
                $scope.load();
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công!',
                    text: 'Danh mục đã được cập nhật thành công.',
                });
            }).catch(error => {
                Swal.fire({
                    icon: 'error',
                    title: 'Lỗi!',
                    text: 'Không thể cập nhật danh mục. Vui lòng thử lại.',
                });
            });
        } 






    $scope.delete = function (id) {
        // Hiển thị cửa sổ xác nhận xóa
        Swal.fire({
            title: 'Xác nhận xóa?',
            text: 'Bạn có chắc muốn xóa danh mục này?',
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Xóa',
            cancelButtonText: 'Hủy bỏ'
        }).then((result) => {
            if (result.isConfirmed) {
                // Nếu người dùng xác nhận xóa
                $http.delete(`${url}/${id}`).then(resp => {
                    // Hiển thị thông báo thành công
                    Swal.fire({
                        icon: 'success',
                        title: 'Xóa thành công!',
                        showConfirmButton: false,
                        timer: 1500  // Tự động đóng sau 1.5 giây
                    });

                    $scope.load();
                }).catch(error => {
                    // Hiển thị thông báo lỗi khi xóa
                    Swal.fire({
                        icon: 'error',
                        title: 'Lỗi khi xóa!',
                        text: 'Đã xảy ra lỗi khi thực hiện xóa danh mục.',
                    });
                });
            }
        });
    };

    $scope.init = function () { }

    $scope.load();
});