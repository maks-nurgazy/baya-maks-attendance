class FingerprintSocket {
    constructor() {

    }

    init() {
        this.socket = new SockJS('/ws');
        this.stompClient = Stomp.over(this.socket);
        this.stompClient.connect({}, this.onConnected.bind(this), this.onError);
    };

    onConnected() {
        this.boolConnected = true;
        this.stompClient.subscribe('/topic/fingerprints', this.onMessageReceived);
    };

    onError() {
        console.log("Error happened");
    }

    onMessageReceived(payload) {
        const message = JSON.parse(payload.body);
        if (message.status==='added'){
            const images = document.querySelector("#images");
            for(let i=0;i<message.number;i++){
                images.children[i].classList.add("new-finger");
            }
        }
        else if(message.status==="full"){
            console.log("Fingerprint are taken... Just save it");
        }
        else if (message.status==="empty"){
            alert("Put your fingers to the scanner");
        }else if (message.status==="saved"){
            const data = document.querySelector("#student-data");
            let input = data.getElementsByTagName("input");
            let label = data.getElementsByTagName("label");
            for (let i=0;i<input.length;i++){
                if (label[i].textContent===message.id){
                    input[i].checked = true;
                }
            }
        }
    }

    sendMessage(message) {
        this.stompClient.send("/app/new-fingerprint", {}, message);
    }


}




class FingerprintController {
    constructor() {
        this.fingerSocket = new FingerprintSocket();
        this.fingerSocket.init();
    }

    setEvents(){
        this.studentId = document.querySelector("#student-id");
        this.images = document.querySelector("#images");
        this.studentData = document.querySelector("#student-data");
        this.disconnect = document.querySelector(".disconnect");
        this.saveFingerprints = document.querySelector("#save-fingerprints");
        this.studentData.addEventListener('click', this.selectedStudent.bind(this));
        this.disconnect.addEventListener('click', this.disConnect.bind(this));
        this.saveFingerprints.addEventListener('click',this.saveStudentFingerprint.bind(this));
    }


    saveStudentFingerprint() {
        this.fingerSocket.sendMessage("save");
    }

    disConnect() {
        this.studentId.placeholder = 'Student Id';
        this.fingerSocket.sendMessage("disconnect");
        this.studentId.classList.remove('student-finger');
        for(let i=0;i<this.images.children.length;i++){
            this.images.children[i].classList.remove("new-finger");
        }
    }

    selectedStudent(event) {
        const element = event.target;
        if (element.type === "submit") {
            this.disConnect();
            const parent = element.parentNode.parentNode.parentNode.parentNode;
            this.currentStudentId = parent.cells[0].getElementsByTagName('label')[0].textContent;
            this.studentId.placeholder = this.currentStudentId;
            this.studentId.classList.add('student-finger');
            this.fingerSocket.sendMessage(this.currentStudentId);
        }
    }


    clearPage() {
        let studentId = document.querySelector("#student-id");
        studentId.placeholder = "Student Id";
        studentId.classList.remove("student-finger");
        let images = document.querySelector("#images");
        for(let i=0;i<this.images.children.length;i++){
            this.images.children[i].classList.remove("new-finger");
        }
        this.fingerSocket.sendMessage("disconnect");
    }
}


