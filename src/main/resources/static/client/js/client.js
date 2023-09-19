$(document).ready(function () {
    $("form").submit(function (event) {
        var nameInput = $("#name");
        var nameError = $("#nameError");

        if (nameInput.val().length < 3 || nameInput.val().length > 250) {
            event.preventDefault();
            nameError.text("Name must be between 3 and 250 characters.");
        } else {
            nameError.text("");
        }
    });
});
$(document).ready(function () {
    $('#address').on('blur', function () {
        var addressValue = $(this).val();
        if (addressValue.length < 10 || addressValue.length > 250) {
            $('#addressError').text('Address must be from 10 to 250 characters.');
        } else {
            $('#addressError').text('');
        }
    });

    $('#phone').on('blur', function () {
        var phoneValue = $(this).val();
        var phonePattern = /^[0-9]{10,30}$/;
        if (!phonePattern.test(phoneValue)) {
            $('#phoneError').text('Phone must be from 10 to 30 numbers (digits only).');
        } else {
            $('#phoneError').text('');
        }
    });
});

document.addEventListener("DOMContentLoaded", function () {
    var filterSelect = document.getElementById('filterLDT');

    // Kiểm tra nếu đã lưu giá trị trong sessionStorage, thì khôi phục nó
    var selectedOption = sessionStorage.getItem('selectedOption');
    if (selectedOption) {
        filterSelect.value = selectedOption;
    }

    // Lưu giá trị khi thay đổi
    filterSelect.addEventListener('change', function () {
        var selectedOption = filterSelect.value;
        sessionStorage.setItem('selectedOption', selectedOption);
    });
});

//var filterSelect = document.getElementById('filterLDT');
//
//// Lưu giá trị option khi thay đổi
//filterSelect.addEventListener('change', function () {
//    var selectedOption = this.value;
//    localStorage.setItem('selectedOption', selectedOption);
//});
//
//// Khôi phục giá trị option từ localStorage khi trang được tải lại
//window.addEventListener('load', function () {
//    var selectedOption = localStorage.getItem('selectedOption');
//    if (selectedOption) {
//        filterSelect.value = selectedOption;
//    }
//});