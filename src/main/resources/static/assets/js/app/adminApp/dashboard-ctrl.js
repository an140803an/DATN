app.controller("dashboard-ctrl", function ($scope, $http) {
  $scope.fnAllOrder = async function () {
    await $http.get("/rest/order/allOrderInCurrentYear").then((resp) => {
      $scope.allOrder = resp.data;
    });
  };

  $scope.fnTotalAmountOrdersInCurrentMonth = async function () {
    await $http.get("/rest/order/totalAmountOrdersInCurrentMonth").then((resp) => {
      $scope.totalAmountOrdersInCurrentMonth = resp.data;
    });
  };

  $scope.fnTotalAmountOrdersInCurrentYear = async function () {
    await $http.get("/rest/order/totalAmountOrdersInCurrentYear").then((resp) => {
      $scope.totalAmountOrdersInCurrentYear = resp.data;
    });
  };

  $scope.fnOrderPieChart = async function () {
    await $http.get("/rest/order/orderPieChartInCurrentYear").then((resp) => {
      var ctx = document.getElementById("orderPieChart");
      var myPieChart = new Chart(ctx, {
        type: "doughnut",
        data: {
          labels: ["COD", "Online Banking"],
          datasets: [
            {
              data: [parseInt(resp.data.COD) , parseInt(resp.data.OnlineBanking)],
              backgroundColor: ["#4e73df", "#1cc88a", "#36b9cc"],
              hoverBackgroundColor: ["#2e59d9", "#17a673", "#2c9faf"],
              hoverBorderColor: "rgba(234, 236, 244, 1)",
            },
          ],
        },
        options: {
          maintainAspectRatio: false,
          tooltips: {
            backgroundColor: "rgb(255,255,255)",
            bodyFontColor: "#858796",
            borderColor: "#dddfeb",
            borderWidth: 1,
            xPadding: 15,
            yPadding: 15,
            displayColors: false,
            caretPadding: 10,
          },
          legend: {
            display: false,
          },
          cutoutPercentage: 80,
        },
      });
    });
  };

  $scope.fnCODOrderBarChart = async function () {
    await $http.get("/rest/order/CODOrderBarChart").then((resp) => {
        var ctx2 = document.getElementById("CODOrderBarChart");
        var arrMonth = resp.data.map((item)=>{return item.month} )
        var arrCancel = resp.data.map((item)=>{return item.cancel} )
        var arrNull = resp.data.map((item)=>{return item.null} )
        var arrSuccess = resp.data.map((item)=>{return item.success} ) 

      

        var myBarChart2 = new Chart(ctx2, {
          type: 'bar',
          data: {
            labels: arrMonth,
            datasets: [{
              label: "Hủy",
              backgroundColor: "#d92e2e",
              hoverBackgroundColor: "#d93c34",
              borderColor: "#d92e2e",
              data: arrCancel,
            },{
              label: "Chờ sử lý",
              backgroundColor: "#e9f542",
              hoverBackgroundColor: "#d9d92e",
              borderColor: "#e9f542",
              data: arrNull,
            },
            {
              label: "Thành công",
              backgroundColor: "#1cc88a",
              hoverBackgroundColor: "#17a673",
              borderColor: "#1cc88a",
              data: arrSuccess,
            }],
          },
          options: {
            maintainAspectRatio: false,
            layout: {
              padding: {
                left: 10,
                right: 25,
                top: 25,
                bottom: 0
              }
            },
            scales: {
              xAxes: [{
                stacked: true,
                time: {
                  unit: 'month'
                },
                gridLines: {
                  display: false,
                  drawBorder: false
                },
                ticks: {
                //   maxTicksLimit: 6
                },
                maxBarThickness: 25,
              }],
              yAxes: [{
                stacked: true,
                ticks: {
                  min: 0,
                  max: 100,
                  maxTicksLimit: 5,
                  padding: 10,
                //   // Include a dollar sign in the ticks
                //   callback: function(value, index, values) {
                //     return '$' + number_format(value);
                //   }
                },
                gridLines: {
                  color: "rgb(234, 236, 244)",
                  zeroLineColor: "rgb(234, 236, 244)",
                  drawBorder: false,
                  borderDash: [2],
                  zeroLineBorderDash: [2]
                }
              }],
            },
            legend: {
              display: false
            },
            tooltips: {
              titleMarginBottom: 10,
              titleFontColor: '#6e707e',
              titleFontSize: 14,
              backgroundColor: "rgb(255,255,255)",
              bodyFontColor: "#858796",
              borderColor: '#dddfeb',
              borderWidth: 1,
              xPadding: 15,
              yPadding: 15,
              displayColors: false,
              caretPadding: 10,
            //   callbacks: {
            //     label: function(tooltipItem, chart) {
            //       var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
            //       return datasetLabel + ': $' + number_format(tooltipItem.yLabel);
            //     }
            //   }
            },
          }
        });
    });
  };

  $scope.fnOnlineBankingOrderBarChart = async function () {
    await $http.get("/rest/order/OnlineBankingOrderBarChart").then((resp) => {
        var ctx2 = document.getElementById("OnlineBankingOrderBarChart");
        var arrMonth = resp.data.map((item)=>{return item.month} )
        var arrCancel = resp.data.map((item)=>{return item.cancel} )
        var arrNull = resp.data.map((item)=>{return item.null} )
        var arrSuccess = resp.data.map((item)=>{return item.success} ) 

        
        var myBarChart2 = new Chart(ctx2, {
          type: 'bar',
          data: {
            labels: arrMonth,
            datasets: [{
              label: "Hủy",
              backgroundColor: "#d92e2e",
              hoverBackgroundColor: "#d93c34",
              borderColor: "#d92e2e",
              data: arrCancel,
            },{
              label: "Chờ sử lý",
              backgroundColor: "#e9f542",
              hoverBackgroundColor: "#d9d92e",
              borderColor: "#e9f542",
              data: arrNull,
            },
            {
              label: "Thành công",
              backgroundColor: "#1cc88a",
              hoverBackgroundColor: "#17a673",
              borderColor: "#1cc88a",
              data: arrSuccess,
            }],
          },
          options: {
            maintainAspectRatio: false,
            layout: {
              padding: {
                left: 10,
                right: 25,
                top: 25,
                bottom: 0
              }
            },
            scales: {
              xAxes: [{
                stacked: true,
                time: {
                  unit: 'month'
                },
                gridLines: {
                  display: false,
                  drawBorder: false
                },
                ticks: {
                //   maxTicksLimit: 6
                },
                maxBarThickness: 25,
              }],
              yAxes: [{
                stacked: true,
                ticks: {
                  min: 0,
                  max: 100,
                  maxTicksLimit: 5,
                  padding: 10,
                //   // Include a dollar sign in the ticks
                //   callback: function(value, index, values) {
                //     return '$' + number_format(value);
                //   }
                },
                gridLines: {
                  color: "rgb(234, 236, 244)",
                  zeroLineColor: "rgb(234, 236, 244)",
                  drawBorder: false,
                  borderDash: [2],
                  zeroLineBorderDash: [2]
                }
              }],
            },
            legend: {
              display: false
            },
            tooltips: {
              titleMarginBottom: 10,
              titleFontColor: '#6e707e',
              titleFontSize: 14,
              backgroundColor: "rgb(255,255,255)",
              bodyFontColor: "#858796",
              borderColor: '#dddfeb',
              borderWidth: 1,
              xPadding: 15,
              yPadding: 15,
              displayColors: false,
              caretPadding: 10,
            //   callbacks: {
            //     label: function(tooltipItem, chart) {
            //       var datasetLabel = chart.datasets[tooltipItem.datasetIndex].label || '';
            //       return datasetLabel + ': $' + number_format(tooltipItem.yLabel);
            //     }
            //   }
            },
          }
        })
    })
  };



  $scope.fnAllOrder()
  $scope.fnTotalAmountOrdersInCurrentMonth()
  $scope.fnTotalAmountOrdersInCurrentYear()
  $scope.fnOrderPieChart()
  $scope.fnCODOrderBarChart()
  $scope.fnOnlineBankingOrderBarChart()
});
