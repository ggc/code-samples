/* Data constants */
capital = 104000;
meses = 20 * 12;
interes = 0.012; // 1.2% = 0.012
cuota = 0;
cuotaRedondeada = 0;
pagos = [];

function euro(value) {
    return Math.round(value * 100) / 100;
}

/* Equation */
function updateCuota(cap, numMeses, ints, amortizaciones = {}) {
    console.log('[LOG] updateCuota: ', amortizaciones);
    cuota = cap * (Math.pow(1 + ints, numMeses) * ints) / (Math.pow(1 + ints, numMeses) - 1)
    cuotaRedondeada = euro(cuota)

    pagos = [{
        mes: 1,
        cuota: cuotaRedondeada,
        intereses: 10.24,
        capitalPendiente: cap,
        amortizado: 0
    }];
    for (let j = 1; j < numMeses; j++) {
        if (pagos[j - 1].capitalPendiente <= cuotaRedondeada) {
            return pagos;
        }
        const hasAmortizacion = amortizaciones[j + 1] !== undefined ? amortizaciones[j + 1] : 0;
        console.log('[LOG] hasAmortizacion: ', hasAmortizacion);
        pagos.push({
            mes: j + 1,
            cuota: cuotaRedondeada,
            intereses: euro(pagos[j - 1].capitalPendiente * ints),
            capitalPendiente: euro(pagos[j - 1].capitalPendiente - cuota + pagos[j - 1].capitalPendiente * ints) - hasAmortizacion,
            amortizado: hasAmortizacion
        })
    }

    return pagos;
}

function parseAmortizaciones(rawText) {
    const amortizacionesAux = rawText.split("\n");
    const amortizaciones = {};

    amortizacionesAux.forEach((payment) => {
        if (payment[0] === '*') {
            payment = payment.replace('*', '');
            const every = +payment.split(",")[0];
            const amount = +payment.split(",")[1];
            for (let i = 0; i < meses; i = i + every) {
                amortizaciones[i] = amount;
            }
            return;
        }
        amortizaciones[payment.split(",")[0]] = +payment.split(",")[1];
    })

    return amortizaciones;
}
