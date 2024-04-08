var app = angular.module("mainApp", []);

app.filter("formatCurrency", [
  "$filter",
  function ($filter) {
    return function (input) {
      return $filter("number")(input, 0).replace(/,/g, ".") + "đ";
    };
  },
]);

  