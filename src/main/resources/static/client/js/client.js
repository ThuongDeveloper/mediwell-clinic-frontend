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