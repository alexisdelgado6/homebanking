const { createApp } = Vue

createApp({
    data() {
        return {
            clientsList : [],
            nameValue : "",
            lastNameValue: "",
            emailValue: "",
        }
    },
    created(){
        this.loadData()
    },
    mounted () {
        
    },
    methods: {
        loadData (){
            axios.get('/api/clients')
            .then(response =>{
                this.clientsList = response.data;
                console.log(this.clientsList)
            })

        },
        addClient (){
            axios.post('/rest/clients', {
                firstName : this.nameValue,
                lastName : this.lastNameValue,
                email : this.emailValue
            })
            .then(newClient =>{
                this.clientsList.push(newClient.data)
            })
        },
        deleteClient(clientSelected) {
            axios.delete("/rest/clients/" + clientSelected.id)
                .then(() => {
                this.loadData()
                })
    },
        editClient(clientSelected){
            let newEmail
            newEmail = prompt("Enter a new email!")
            client = {
                email: newEmail
            }
            axios.patch("/rest/clients/" + clientSelected.id, client)
            .then(()=> this.loadData())
        },
},  
    computed: {
    },
}).mount('#app')