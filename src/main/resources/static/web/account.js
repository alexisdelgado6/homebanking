const { createApp } = Vue

createApp({
    data() {
        return {
            clients: [],
            transactions: [],
            accounts: [],
            queryString: "",
            params: "",
            id: "",
            initialFirstName: "",
            initialLastname:"",
            fromDate:"",
            toDate:"",
        }
    },
    created(){
        this.queryString = location.search;
        this.params = new URLSearchParams(this.queryString);
        this.id = this.params.get('id');
        this.loadClient();
        this.loadData();
    },
    mounted () {
    },
    methods: {
        loadClient(){
            axios.get('/api/clients/current')
            .then(response =>{
                this.clients = response.data
                this.initialFirstName = this.clients.firstName.slice(0,1).toUpperCase()
                this.initialLastname = this.clients.lastName.slice(0,1).toUpperCase()
            })
        },
        loadData (){
            axios.get('/api/account/' + this.id)
            .then(response =>{
                this.accounts = response.data
                this.transactions = this.accounts.transaction.sort((a,b) => a.id-b.id)
                this.normalizeDate(this.transactions)
                console.log(this.accounts.number)
            })
        },
        normalizeDate(dateTransactions){
            dateTransactions.forEach(date => {
                date.date = date.date.slice(0,10)
            });
        },
        logout(){
            axios.post('/api/logout')
            .then(()=>{
                window.location.href='/web/index.html'
            })
        },
        pdf(){
            this.fromDate = new Date(this.fromDate).toISOString()
            this.toDate = new Date(this.toDate).toISOString()
            axios.post('/api/transactions/filter',{fromDate:`${this.fromDate}`,toDate:`${this.toDate}`,account:`${this.accounts.number}`})
            .then(() => Swal.fire({
                position: 'center',
                icon: 'success',
                title: 'PDF successfully generated, check out your desktop.',
                timer: 4000
            }))
            .then(() => {
                window.location.reload()
            })
            .catch(error =>
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text:  `${error.response.data}`,
                }))
            .then(()=>
            window.location.reload()
            )
        },
},
    computed: {
    },
}).mount('#app')