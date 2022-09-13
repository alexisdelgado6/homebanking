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
            loans: [],
            initialFirstName: "",
            initialLastname: "",
            activeAccounts: [],
            accountNumber: "",
            accountTypeChoosed: "",
            fromDate:"",
            toDate:"",
            accountPDF:"",
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
                    this.initialFirstName = this.client.firstName.slice(0, 1).toUpperCase()
                    this.initialLastname = this.client.lastName.slice(0, 1).toUpperCase()
                    this.accounts = this.client.accounts.sort((a, b) => a.id - b.id)
                    this.activeAccounts = this.accounts.filter(e => e.acountStatus)
                    this.loans = this.client.loans.sort((a, b) => a.id - b.id)
                    this.normalizeDate(this.accounts)
                    console.log(this.activeAccounts)
                })
        },
        normalizeDate(date) {
            date.forEach(d => {
                d.creationDate = d.creationDate.slice(0, 10)
            });
        },
        logout() {
            axios.post('/api/logout')
                .then(() => {
                    window.location.href = '/web/index.html'
                })
        },
        createAccount() {
            axios.post('/api/clients/current/accounts', `accountType=${this.accountTypeChoosed}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                .then(() => Swal.fire({
                    position: 'center',
                    icon: 'success',
                    title: 'Your account has been created.',
                    showConfirmButton: false,
                    timer: 1500
                }))
                .then(() => {
                    window.location.reload()
                })
                .catch(error =>
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: 'Something went wrong!',
                        footer: '<a href="">Why do I have this issue?</a>'
                    }))
        },
        deletedAccount() {
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
                confirmButtonText: 'Yes, deleted account!',
                cancelButtonText: 'No, cancel!',
                reverseButtons: true
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.patch('/api/clients/current/accounts', `number=${this.accountNumber}`, { headers: { 'content-type': 'application/x-www-form-urlencoded' } })
                        .then(() =>
                            swalWithBootstrapButtons.fire(
                                'Great job!',
                                'The account has been deleted',
                                'success'
                            ))
                        .then(() =>
                            window.location.reload())
                        .catch((error) =>
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
                        'The account was not deleted',
                        'error'
                    )
                }
            })
        },
        pdf(){
            this.fromDate = new Date(this.fromDate).toISOString()
            this.toDate = new Date(this.fromDate).toISOString()
            axios.post('/api/transactions/filter',{fromDate:`${this.fromDate}`,toDate:`${this.toDate}`,account:`${this.accountPDF}`})
            .then(()=>
            window.location.reload()
            )
        },
    },
    computed: {
    },
}).mount('#app')