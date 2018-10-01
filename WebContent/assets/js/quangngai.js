var lengendData = {
    "data": [
        {
            "color" : "#A57B01",
            "name" : "Đất nhóm nhà ở",
        },
        {
            "color" : "#0C7F00",
            "name" : "Đất cây xanh",
        },
        {
            "color" : "#A45100",
            "name" : "Đất cơ quan, công ty",
        },
        {
            "color" : "#FFBF01",
            "name" : "Đất chung cư",
        },
        {
            "color" : "#7E2001",
            "name" : "Đất trường học",
        },
        {
            "color" : "#7F5F00",
            "name" : "Đất trung tâm nghiên cứu đào tạo",
        },
        {
            "color" : "#4C4B00",
            "name" : "Đất biệt thự",
        },
        {
            "color" : "#FE1400",
            "name" : "Đất công cộng đô thị",
        },
        {
            "color" : "#FE1400",
            "name" : "Đất công cộng đơn vị ở",
        },
        {
            "color" : "#FE9F7E",
            "name" : "Đất hỗn hợp",
        },
        {
            "color" : "#FD2BFF",
            "name" : "Đất văn hóa",
        },
        {
            "color" : "#7E2001",
            "name" : "Đất trường THPT",
        },
        {
            "color" : "#7E3F3F",
            "name" : "Đất trường THCS, tiểu học, mầm non",
        },
        {
            "color" : "#52A502",
            "name" : "Đất cây xanh đô thị",
        },
        {
            "color" : "#264C00",
            "name" : "Đất cây xanh đơn vị ở",
        },
    ]
};


function openTab(evt, cityName) {
  $('.tabcontent').removeClass('active');
  $('#' + cityName).addClass('active');
    // Declare all variables
    var i, tabcontent, tablinks;

    // Get all elements with class="tabcontent" and hide them
    tabcontent = document.getElementsByClassName("tabcontent");
    for (i = 0; i < tabcontent.length; i++) {
        tabcontent[i].style.display = "none";
    }

    // Get all elements with class="tablinks" and remove the class "active"
    tablinks = document.getElementsByClassName("tablinks");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" active", "");
    }

    // Show the current tab, and add an "active" class to the button that opened the tab
    document.getElementById(cityName).style.display = "block";
    evt.currentTarget.className += " active";
}

$(document).ready(function() {
var html = "<ul>";
$.each(lengendData.data, function (index, item) {
    html += "<li><span class='item-color' style='background-color:"+ item.color +"'></span><span class='item-name'>"+ item.name +"</span></li>";
});
html += "</ul>";
$("#lengends_result").append(html);
});


