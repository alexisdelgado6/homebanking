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
            loans: [],
            loanName: [],
            loanNameChoose: "",
            loanCar: {},
            loanPersonal: {},
            loanMortgage: {},
            loanPayments: "",
            loanAmount: "",
            accountDestiny: "",
            totalWithTax: '',
            fromDate:"",
            toDate:"",
            accountPDF:"",
        }
    },
    created(){
        this.queryString = location.search;
        this.params = new URLSearchParams(this.queryString);
        this.id = this.params.get('id');
        this.loadClient();
        this.loadData();
        this.loadLoans();
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
        loadData() {
            axios.get('/api/clients/current')
                .then(response => {
                    this.client = response.data
                    this.accounts = this.client.accounts.sort((a, b) => a.id - b.id)
                    this.loans = this.client.loans.sort((a, b) => a.id - b.id)
                    this.initialFirstName = this.client.firstName.slice(0,1).toUpperCase()
                    this.initialLastname = this.client.lastName.slice(0,1).toUpperCase()
                })
        },
        logout(){
            axios.post('/api/logout')
            .then(()=>{
                window.location.href='/web/index.html'
            })
        },
        loadLoans(){
            axios.get('/api/loans')
            .then((response)=>{
                this.loans = response.data
                this.loanCar = this.loans.filter(loan=> loan.name == 'Car')
                this.loanMortgage = this.loans.filter(loan => loan.name == 'Mortgage')
                this.loanPersonal = this.loans.filter(loan => loan.name == 'Personal')
            })
        },
        getLoan() {
            const swalWithBootstrapButtons = Swal.mixin({
                customClass: {
                    confirmButton: 'btn btn-success',
                    cancelButton: 'btn btn-danger'
                },
                buttonsStyling: false
            })
            swalWithBootstrapButtons.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Yes, aplly loan!',
                cancelButtonText: 'No, cancel!',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.post("/api/loans",{name: this.loanNameChoose, amount: this.loanAmount, payments: this.loanPayments, accountNumberDestiny: this.accountDestiny})
                    .then(()=>
                    swalWithBootstrapButtons.fire(
                        'Great job!',
                        'The loan was sucesfull',
                        'success'
                    ))
                    .then(() =>
                    window.location.href='./accounts.html')
                    .catch((error)=>
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: `${error.response.data}`,
                    }))
                } else if (
                    result.dismiss === Swal.DismissReason.cancel
                ) {
                    swalWithBootstrapButtons.fire(
                        'Cancelled',
                        'The loan was cancelled!',
                        'error'
                    )
                }
            })
        },
        personalInfo(){
            Swal.fire(
                'Personal Loan',
                'You can request up to $100.000,00.',
                'question'
            )
        },
        mortgageInfo(){
            Swal.fire(
                'Mortgage Loan',
                'You can request up to $500.000,00.',
                'question'
            )
        },
        carInfo(){
            Swal.fire(
                'Car Loan',
                'You can request up to $300.000,00.',
                'question'
            )
        },
},
    computed: {
        tax(){
            if(this.loanNameChoose == 'Car' && this.loanPayments == 6){
                this.totalWithTax = ((this.loanAmount * 1.25) / this.loanPayments)
            }
            if(this.loanNameChoose == 'Car' && this.loanPayments == 12){
                this.totalWithTax = ((this.loanAmount * 1.30) / this.loanPayments)
            }
            if(this.loanNameChoose == 'Car' && this.loanPayments == 24){
                this.totalWithTax = ((this.loanAmount * 1.35) / this.loanPayments)
            }
            if(this.loanNameChoose == 'Car' && this.loanPayments == 36){
                this.totalWithTax = ((this.loanAmount * 1.40) / this.loanPayments)
            }
            if(this.loanNameChoose == 'Personal' && this.loanPayments == 12){
                this.totalWithTax = ((this.loanAmount * 1.25) / this.loanPayments)
            }
            if(this.loanNameChoose == 'Personal' && this.loanPayments == 24){
                this.totalWithTax = ((this.loanAmount * 1.30) / this.loanPayments)
            }
            if(this.loanNameChoose == 'Mortgage' && this.loanPayments == 12){
                this.totalWithTax = ((this.loanAmount * 1.25) / this.loanPayments)
            }
            if(this.loanNameChoose == 'Mortgage' && this.loanPayments == 24){
                this.totalWithTax = ((this.loanAmount * 1.30) / this.loanPayments)
            }
            if(this.loanNameChoose == 'Mortgage' && this.loanPayments == 36){
                this.totalWithTax = ((this.loanAmount * 1.40) / this.loanPayments)
            }
            if(this.loanNameChoose == 'Mortgage' && this.loanPayments == 48){
                this.totalWithTax = ((this.loanAmount * 1.45) / this.loanPayments)
            }
            if(this.loanNameChoose == 'Mortgage' && this.loanPayments == 60){
                this.totalWithTax = ((this.loanAmount * 1.50) / this.loanPayments)
            } return Math.round(this.totalWithTax)
        },
    },
}).mount('#app')