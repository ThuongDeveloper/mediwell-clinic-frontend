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

// Lắng nghe sự kiện "change" trên thẻ select
document.getElementById("typeDoctorFilter").addEventListener("change", function () {
    var selectedTypeDoctor = this.value; // Lấy giá trị đã chọn
    filterDoctors(selectedTypeDoctor);
});

// Hàm để lọc danh sách bác sĩ
function filterDoctors(selectedTypeDoctor) {
    var doctorElements = document.querySelectorAll(".getAllDoctors .widget-doctor");
    
    doctorElements.forEach(function (doctorElement) {
        var typeDoctorElement = doctorElement.querySelector(".widget-title-doctor h4");
        if (selectedTypeDoctor === "" || typeDoctorElement.textContent === selectedTypeDoctor) {
            doctorElement.style.display = "block"; // Hiển thị bác sĩ
            doctorElement.classList.add("filtered"); // Thêm lớp CSS cho phần tử sau khi lọc
        } else {
            doctorElement.style.display = "none"; // Ẩn bác sĩ
            doctorElement.classList.remove("filtered"); // Xóa lớp CSS khỏi phần tử sau khi lọc
        }
    });
}

// Khởi tạo danh sách bác sĩ ban đầu
filterDoctors("");
