/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/javascript.js to edit this template
 */
function calcRate(r) {
 const f = ~~r,//Tương tự Math.floor(r)
 id = 'star' + f + (r % f ? 'half' : '');
 id && (document.getElementById(id).checked = !0);
}


