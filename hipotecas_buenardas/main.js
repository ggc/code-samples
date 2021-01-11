
pagos = updateCuota(capital, meses, interes / 12);

render(pagos);

/* Vue */
data = {
    capital: capital,
    meses: meses,
    interes: interes * 100,
    totalIntereses: euro(pagos.reduce((a, b) => a + b.intereses, 0)),
    cuota: cuotaRedondeada,
    amortizaciones: "*3,900\n*12,2000",
    pagos: pagos//.filter((_, i) => i % 12 == 0)
}
var app = new Vue({
    el: '#app',
    data: data,
    methods: {
        update: function () {
            const amortizaciones = parseAmortizaciones(this.amortizaciones);

            this.pagos = updateCuota(this.capital, this.meses, this.interes / 100 / 12, amortizaciones);
            this.cuota = euro(pagos[0].cuota);
            this.totalIntereses = euro(pagos.reduce((a, b) => a + b.intereses, 0));

            render(this.pagos)
        }
    },
})
