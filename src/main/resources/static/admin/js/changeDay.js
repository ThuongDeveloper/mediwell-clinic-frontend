/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */
const DAYS_OF_WEEK = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];

const dateInput = document.getElementById("date");
const dayInput = document.getElementById("day");


dateInput.addEventListener("change", () => {
  const date = dateInput.value;
  console.log("check date: ", date);

  const day = new Date(date).getDay();
  dayInput.value = DAYS_OF_WEEK[day];
});

