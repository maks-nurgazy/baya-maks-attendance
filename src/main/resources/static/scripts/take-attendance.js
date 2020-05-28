$(document).ready(function () {

    let selected_subject = $('#selected_subject');
    //call function when page is loaded
    getContent();
    //set on change listener
    selected_subject.change(getContent);

    function getContent() {
        //create url to request fragment
        let url = "/teacher/students";
        if (selected_subject.val() !== "") {
            // let s = encodeURIComponent(selected_subject.val());
            //load fragment and replace content
            url += "?subjectId=" + selected_subject.val();
            $('#student-list-section').load(url, function () {


                let subject = "Bilgisayar";
                const subjectInfo = document.querySelector("#subject-info");
                subjectInfo.textContent = subject;


                if ($.fn.DataTable !== undefined) {
                    $('.data-table').DataTable({
                        paging: true,
                        searching: true,
                        info: true,
                        lengthChange: true,
                        lengthMenu: [10, 20, 50, 75, 100],
                        columnDefs: [{
                            targets: [0, -1], // column or columns numbers
                            orderable: false // set orderable for selected columns
                        }],
                    });
                }

                $.fn.extend({
                    toggleHtml: function (a, b) {
                        return this.html(this.html() == b ? a : b);
                    }
                });

                $(".checkAll").on("click", function () {
                    $(this).parents('.table').find('input:checkbox').prop('checked', this.checked);
                    let badge = null;
                    if (this.checked) {
                        badge = $(this).parents('.table').find('td.badge').html("Present");
                    } else {
                        badge = $(this).parents('.table').find('td.badge').html("Absent");
                    }

                    if (badge.html() === "Present") {
                        badge.removeClass("badge-danger");
                        badge.addClass("badge-success");
                    } else {
                        badge.removeClass("badge-success");
                        badge.addClass("badge-danger");
                    }
                });

            });
        }
    }
});

class SocketController {

    constructor() {
        this.init();
    }

    init() {
        this.socket = new SockJS('/ws');
        this.stompClient = Stomp.over(this.socket);
        this.stompClient.connect({}, this.onConnected.bind(this), this.onError);
    };

    onConnected() {
        this.stompClient.subscribe('/topic/attendance', this.onMessageReceived);
    };

    onError() {
        console.log("Error happened");
    }

    onMessageReceived(payload) {
        const message = payload.body;
        let studentList = document.querySelector("#student-list").getElementsByTagName("label");
        for (let i = 1; i < studentList.length; i++) {
            if (studentList[i].textContent === message) {
                studentList[i].parentNode.querySelector("input").checked = true;
                let badge = studentList[i].parentNode.parentNode.parentNode.querySelector("td.badge");
                badge.textContent = "Present";
                badge.classList.remove('badge-danger');
                badge.classList.add('badge-success');

            }
        }
    }

    sendMessage(message) {
        this.stompClient.send("/app/check-fingerprint", {}, message);
    }


}

class AttendanceController {

    constructor() {
        this.socketController = new SocketController();
        document.querySelector("#student-list-section").addEventListener('click', this.selectedStudent.bind(this));
        document.querySelector("#save-attendance").addEventListener('click', this.saveAttendance.bind(this));
    }

    whenSubjectChanged() {
        let subjectId = document.querySelector("#selected_subject");
        subjectId.onchange = function () {
            this.socketController.sendMessage(subjectId.value);
        }.bind(this);
    }

    selectedStudent(event) {
        const element = event.target;
        let ch = element.classList.contains("checkAll");
        if (element.type === "checkbox" && !ch) {
            const parent = element.parentNode.parentNode.parentNode;
            let badge = parent.querySelector("td.badge");
            if (element.checked) {
                badge.textContent = "Present";
                badge.classList.remove("badge-danger");
                badge.classList.add("badge-success");
            } else {
                badge.textContent = "Absent";
                badge.classList.remove("badge-success");
                badge.classList.add("badge-danger");
            }
        }
    }

    saveAttendance() {
        let subjectId = document.querySelector("#selected_subject").value;
        let subjectType = document.querySelector("#subType").value;
        let date = document.querySelector("#subDate").value;
        let myTab = document.getElementById('student-list');
        let allStudent = [];
        for (let i = 1; i < myTab.rows.length; i++) {
            let objCells = myTab.rows.item(i).cells;
            let stId = objCells.item(0).getElementsByTagName("label")[0].textContent;
            let firstName = objCells.item(2).textContent;
            let lastName = objCells.item(3).textContent;
            let status = objCells.item(4).textContent;
            allStudent.push({
                stId: stId,
                firstName: firstName,
                lastName: lastName,
                status: status.toUpperCase(),
                subjectId:subjectId,
                subjectType:subjectType,
                date:date
            });
        }
        $.ajax({
            url: '/teacher/series',
            method: 'POST',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(allStudent)
        })
            .done(function (myObjects) {
                alert("Attendance Saved");
            })
            .fail(function () {
                alert("Not Saved");
            });
    }

}

const attendanceController = new AttendanceController();
attendanceController.whenSubjectChanged();




