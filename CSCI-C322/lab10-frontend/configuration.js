const mode = 0;

const host_local = "http://localhost:8080";
const host_remote = "https://ducks-service-???.onrender.com";

function getHost() {
    return (mode == 0) ? host_local : host_remote;
}

let configuration = {loggedIn: false, hosts: getHost(), token: ""};