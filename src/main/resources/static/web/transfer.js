const { createApp } = Vue

createApp({
    data() {
        return {
            clients: [],
            client: {},
            accounts: [],
            queryString: "",
            params: "",
            id: "",
            transferType: "",
            accountTo: "",
            description: "",
            amount: 0,
            accountFrom: "",
            initialFirstName: "",
            initialLastname:"",
            activeAccounts:[],
        }
    },
    created() {
        this.queryString = location.search;
        this.params = new URLSearchParams(this.queryString);
        this.id = this.params.get('id');
        this.loadData()
    },
    mounted() {

    },
    methods: {
        loadData() {
            axios.get('/api/clients/current')
                .then(response => {
                    this.client = response.data
                    this.accounts = this.client.accounts.sort((a, b) => a.id - b.id)
                    this.loans = this.client.loans.sort((a, b) => a.id - b.id)
                    this.activeAccounts = this.accounts.filter(e => e.acountStatus)
                    this.initialFirstName = this.client.firstName.slice(0,1).toUpperCase()
                    this.initialLastname = this.client.lastName.slice(0,1).toUpperCase()
                    this.normalizeDate(this.accounts)
                })
        },
        normalizeDate(fechitax) {
            fechitax.forEach(fechipiola => {
                fechipiola.creationDate = fechipiola.creationDate.slice(0, 10)
            });
        },
        logout() {
            axios.post('/api/logout')
                .then(() => {
                    window.location.href = '/web/index.html'
                })
        },
        createTransaction() {
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
                confirmButtonText: 'Yes, tranfer it!',
                cancelButtonText: 'No, cancel!',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    accountTo = this.accountTo.toUpperCase()
                    axios.post('/api/transactions', `amount=${this.amount}&description=${this.description}&numberAccountOrigin=${this.accountFrom}&numberAccountReceive=${accountTo}`)
                    .then(()=>
                    swalWithBootstrapButtons.fire(
                        'Great job!',
                        'The transaction was sucesfull',
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
                        'The transfer was cancelled!',
                        'error'
                    )
                }
            })
        },
    },
    computed: {
    },
}).mount('#app')