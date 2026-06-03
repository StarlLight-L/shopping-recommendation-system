// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { getAuth, GoogleAuthProvider } from "firebase/auth";

// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
    apiKey: "AIzaSyANDTNbqDd56WpaKF_LygRzykzew_5sf7s",
    authDomain: "imall-667d4.firebaseapp.com",
    projectId: "imall-667d4",
    storageBucket: "imall-667d4.firebasestorage.app",
    messagingSenderId: "422793429515",
    appId: "1:422793429515:web:074594d3e04291b661fd0e",
    //measurementId: "G-V36MNREGZE"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const provider = new GoogleAuthProvider();

export { auth, provider };