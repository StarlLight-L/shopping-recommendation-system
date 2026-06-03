<template>
  <div class="page-wrapper">
    <h2>Weekly Coding Challenge #1: Sign in/up Form</h2>
    <div class="container" :class="{ 'right-panel-active': isRightPanelActive }" id="container">
      <div class="form-container sign-up-container">
        <form action="#" @submit.prevent="handleSignUpSubmit">
          <h1>Create Account</h1>
          <div class="social-container">
            <a href="#" class="social"><i class="fab fa-facebook-f"></i></a>
            <a href="#" class="social" @click.prevent="handleGoogleLogin"><i class="fab fa-google"></i></a>
            <a href="#" class="social"><i class="fab fa-linkedin-in"></i></a>
          </div>
          <span>or use your email for registration</span>
          <div class="input-group">
            <input type="text" v-model="signUpForm.username" placeholder="Username" />
            <span class="error-message" v-if="formErrors.username">{{ formErrors.username }}</span>
          </div>
          <div class="input-group">
            <input type="email" v-model="signUpForm.email" placeholder="Email" />
            <span class="error-message" v-if="formErrors.email">{{ formErrors.email }}</span>
          </div>
          <div class="input-group">
            <input type="password" v-model="signUpForm.password" placeholder="Password" />
            <span class="error-message" v-if="formErrors.password">{{ formErrors.password }}</span>
          </div>
          <button type="submit">Sign Up</button>
        </form>
      </div>
      <div class="form-container sign-in-container">
        <form action="#" @submit.prevent="handleSignInSubmit">
          <h1>Sign in</h1>
          <div class="social-container">
            <a href="#" class="social"><i class="fab fa-facebook-f"></i></a>
            <a href="#" class="social" @click.prevent="handleGoogleLogin"><i class="fab fa-google"></i></a>
            <a href="#" class="social"><i class="fab fa-linkedin-in"></i></a>
          </div>
          <span>or use your account</span>
          <input type="email" v-model="signInForm.email" placeholder="Email" />
          <input type="password" v-model="signInForm.password" placeholder="Password" />
          <a href="#" @click.prevent="handleForgotPassword">Forgot your password?</a>
          <button type="submit">Sign In</button>
        </form>
      </div>
      <div class="overlay-container">
        <div class="overlay">
          <div class="overlay-panel overlay-left">
            <h1>Welcome Back!</h1>
            <p>To keep connected with us please login with your personal info</p>
            <button class="ghost" @click="handleSignIn">Sign In</button>
          </div>
          <div class="overlay-panel overlay-right">
            <h1>Hello, Friend!</h1>
            <p>Enter your personal details and start journey with us</p>
            <button class="ghost" @click="handleSignUp">Sign Up</button>
          </div>
        </div>
      </div>
    </div>

    <footer>
      <p>
        Click <i class="fa fa-heart" @click="triggerHeartAnimation" :class="{ 'animate-heart': isAnimating }"></i> for This cute website of Electronic Mall
      </p>
    </footer>
  </div>
</template>

<script>
import { user } from '../api/user.js';

import { signInWithPopup } from "firebase/auth";
import { auth, provider } from "../api/firebase.js";

export default {
  name: 'LoginRegister',
  data() {
    return {
      isRightPanelActive: false,
      isAnimating: false,
      signUpForm: {
        name: '',
        email: '',
        password: ''
      },
      signInForm: {
        email: '',
        password: ''
      },
      formErrors: {
        name: '',
        email: '',
        password: ''
      }
    }
  },
  methods: {
    handleSignUp() {
      this.isRightPanelActive = true;
    },
    handleSignIn() {
      this.isRightPanelActive = false;
    },
    validateForm() {
      let isValid = true;
      this.formErrors = {
        username: '',
        email: '',
        password: ''
      };
  
      const usernameRegex = /^[a-zA-Z0-9_]{3,20}$/;
      if (!usernameRegex.test(this.signUpForm.username)) {
        this.formErrors.username = 'Username must be 3-20 characters and can only contain letters, numbers, and underscores';
        isValid = false;
      }
  
      const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      if (!emailRegex.test(this.signUpForm.email)) {
        this.formErrors.email = 'Please enter a valid email address';
        isValid = false;
      }
  
      if (!this.signUpForm.password || this.signUpForm.password.length > 16) {
        this.formErrors.password = 'Password must be between 6 and 16 characters';
        isValid = false;
      }
  
      return isValid;
    },
    async handleSignUpSubmit() {
      if (!this.validateForm()) {
        return;
      }
      try {
        const response = await user.register(this.signUpForm);
  
        alert('Registration successful!');
        this.handleSignIn();
        this.signUpForm = {
          username: '',
          email: '',
          password: ''
        };
        this.formErrors = {
          username: '',
          email: '',
          password: ''
        };
      } catch (error) {
        console.error('Registration failed:', error);
        alert('Registration failed. Please check your internet connection');
      }
    },
    async handleSignInSubmit() {
      try {
        const data = await user.login(this.signInForm);
        this.$store.dispatch('login', {
          user: data.user,
          token: data.token
        });
        this.$router.push('/');
      
      } catch (error) {
        console.error('Login failed:', error);
        alert('Login failed. Please check your internet connection');
      }
    },
    handleForgotPassword() {
      alert('Password reset is not available yet. Please contact the administrator');
    },
    triggerHeartAnimation() {
      if (!this.isAnimating) {
        this.isAnimating = true;
        setTimeout(() => {
          this.isAnimating = false;
        }, 500);
      }
    },

    async handleGoogleLogin() {
      try {
        const result = await signInWithPopup(auth, provider);

        const googleUser = result.user;

        const data = await user.googleLogin({
          username: googleUser.displayName,
          email: googleUser.email,
          avatar: googleUser.photoURL
        });

        console.log(data);

        this.$store.dispatch('login', {
          user: data.user,
          token: data.token
        });

        this.$router.push('/');

      } catch (error) {
        console.error(error);
        alert(error.message);
      }
    }
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css?family=Montserrat:400,800');

* {
  box-sizing: border-box;
}

body {
  background: #f6f5f7;
  font-family: 'Montserrat', sans-serif;
  margin: 0;
  width:960px;
  height:1000px;
}

.page-wrapper {
  min-height: 100vh;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #f6f5f7;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.container {
  background-color: #fff;
  border-radius: 20px;
  box-shadow: 0 14px 28px rgba(0,0,0,0.1);
  position: relative;
  overflow: hidden;
  width: 850px;
  max-width: 90%;
  min-height: 480px;
  margin: auto;
}

h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
  font-size: 24px;
  position: relative;
  margin-top: -60px;
}

h1 {
  font-weight: bold;
  margin: 0 0 20px 0;
  font-size: 32px;
  color: #333;
}

p {
  font-size: 14px;
  font-weight: 100;
  line-height: 20px;
  letter-spacing: 0.5px;
  margin: 20px 0 30px;
}

span {
  font-size: 12px;
}

a {
  color: #333;
  font-size: 14px;
  text-decoration: none;
  margin: 15px 0;
}

button {
  border-radius: 50px;
  border: 1px solid #FF4B2B;
  background-color: #FF4B2B;
  color: #FFFFFF;
  font-size: 14px;
  font-weight: bold;
  padding: 12px 45px;
  letter-spacing: 1px;
  text-transform: uppercase;
  margin-top: 15px;
  transition: transform 80ms ease-in;
  cursor: pointer;
}

button:active {
  transform: scale(0.95);
}

button:focus {
  outline: none;
}

button.ghost {
  background-color: transparent;
  border: 2px solid #fff;
  color: #fff;
}

form {
  background-color: #FFFFFF;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  padding: 0 50px;
  height: 100%;
  text-align: center;
}

input {
  background-color: #f5f5f5;
  border: none;
  border-radius: 8px;
  padding: 15px 20px;
  margin: 8px 0;
  width: 100%;
  font-size: 14px;
}

.form-container {
  position: absolute;
  top: 0;
  height: 100%;
  transition: all 0.6s ease-in-out;
}

.sign-in-container {
  left: 0;
  width: 50%;
  z-index: 2;
  padding: 0 50px;
}

.container.right-panel-active .sign-in-container {
  transform: translateX(100%);
}

.sign-up-container {
  left: 0;
  width: 50%;
  opacity: 0;
  z-index: 1;
}

.container.right-panel-active .sign-up-container {
  transform: translateX(100%);
  opacity: 1;
  z-index: 5;
  animation: show 0.6s;
}

@keyframes show {
  0%, 49.99% {
    opacity: 0;
    z-index: 1;
  }
  
  50%, 100% {
    opacity: 1;
    z-index: 5;
  }
}

.overlay-container {
  position: absolute;
  top: 0;
  left: 50%;
  width: 50%;
  height: 100%;
  overflow: hidden;
  transition: transform 0.6s ease-in-out;
  z-index: 100;
}

.container.right-panel-active .overlay-container{
  transform: translateX(-100%);
}

.overlay {
  background: #FF416C;
  background: linear-gradient(45deg, #FF4B2B, #FF416C);
  color: #FFFFFF;
  position: relative;
  left: -100%;
  height: 100%;
  width: 200%;
  transform: translateX(0);
  transition: transform 0.6s ease-in-out;
}

.container.right-panel-active .overlay {
  transform: translateX(50%);
}

.overlay-panel {
  position: absolute;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  padding: 0 40px;
  text-align: center;
  top: 0;
  height: 100%;
  width: 50%;
  transform: translateX(0);
  transition: transform 0.6s ease-in-out;
}

.overlay-panel h1 {
  color: #fff;
  font-size: 40px;
  margin-bottom: 15px;
}

.overlay-panel p {
  color: #fff;
  font-size: 16px;
  line-height: 1.5;
  margin: 20px 0 30px;
}

.social-container {
  margin: 20px 0 30px 0;
}

.social-container a {
  border: 1px solid #ddd;
  border-radius: 50%;
  display: inline-flex;
  justify-content: center;
  align-items: center;
  margin: 0 8px;
  height: 45px;
  width: 45px;
  transition: all 0.3s ease;
}

.social-container a:hover {
  background-color: #f5f5f5;
}

footer {
  background-color: #222;
  color: #fff;
  font-size: 14px;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  text-align: center;
  z-index: 999;
  padding: 10px 0;
}

footer p {
  margin: 10px 0;
}

footer i {
  color: red;
}

footer a {
  color: #3c97bf;
  text-decoration: none;
}

.overlay-left {
  transform: translateX(0);
  left: 0;
}

.container.right-panel-active .overlay-left {
  transform: translateX(0);
}

.overlay-right {
  right: 0;
  transform: translateX(0);
}

.container.right-panel-active .overlay-right {
  transform: translateX(0);
}

footer i.fa-heart {
  color: #ff4b4b;
  cursor: pointer;
  transition: all 0.3s ease;
  transform-origin: center;
}

footer i.fa-heart:hover {
  transform: scale(1.2);
}

footer i.fa-heart.animate-heart {
  animation: heartBeat 0.5s ease-in-out;
}

@keyframes heartBeat {
  0% {
    transform: scale(1);
  }
  25% {
    transform: scale(1.3);
  }
  50% {
    transform: scale(1);
  }
  75% {
    transform: scale(1.3);
  }
  100% {
    transform: scale(1);
  }
}
.input-group {
  width: 100%;
  margin-bottom: 10px;
}

.error-message {
  color: #ff4b2b;
  font-size: 12px;
  margin-top: 5px;
  display: block;
}
</style>
