const { createApp } = Vue

createApp({
    data() {
        return {
            email: "",
            password: "",
            newName:"",
            newLastName:"",
            newEmail:"",
            newPassword:"",
        }
    },
    created(){
      
    },
    mounted () {
    },
    methods: {
      login(){
        axios.post('/api/login', `email=${this.email}&password=${this.password}`)
        .then(()=> {
          window.location.href='/web/accounts.html'
        })
        .catch(error =>
          Swal.fire({
              icon: 'error',
              title: 'Oops...',
              text: 'The email or password is incorrect',
          }))
      },
      singUp(){
        axios.post('/api/clients',`firstName=${this.newName}&lastName=${this.newLastName}&email=${this.newEmail}&password=${this.newPassword}`)
        .then(()=>
        axios.post('/api/login',`email=${this.newEmail}&password=${this.newPassword}`))
        .then (() =>Swal.fire({
          position: 'center',
          icon: 'success',
          title: 'Your account has been created.',
          showConfirmButton: false,
          timer: 1500
      }))
        .then(()=> window.location.href='/web/accounts.html')
        .catch(error =>
          Swal.fire({
              icon: 'error',
              title: 'Oops...',
              text: `${error.response.data}`,
          }))
        },
},
    computed: {
    },
}).mount('#app')

const sign_in_btn = document.querySelector("#sign-in-btn");
const sign_up_btn = document.querySelector("#sign-up-btn");
const container = document.querySelector(".container");

sign_up_btn.addEventListener("click", () => {
  container.classList.add("sign-up-mode");
});

sign_in_btn.addEventListener("click", () => {
  container.classList.remove("sign-up-mode");
});
