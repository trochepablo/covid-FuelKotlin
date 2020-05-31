<template>
  <q-page class="flex flex-center">
    <div>
      <div class="q-pa-md row items-start q-gutter-md">
        <q-card class="my-card">
          <q-card-section>
            <apexchart width="1000" type="line" :options="options" :series="options.series"></apexchart>
          </q-card-section>
        </q-card>
      </div>
      <div class="q-pa-md row items-start q-gutter-md">
        <q-card class="my-card">
          <q-card-section>
            <apexchart width="800" type="pie" :options="chartOptions" :series="totales"></apexchart>
          </q-card-section>
        </q-card>
      </div>
    </div>
  </q-page>
</template>

<script>

import VueApexCharts from 'vue-apexcharts'

export default {
  name: 'PageIndex',
  components: {
    apexchart: VueApexCharts
  },
  data () {
    return {
      options: {
        title: {
          text: 'Historial de casos en Argentina',
          align: 'left'
        },
        chart: {
          id: 'vuechart-example'
        },
        xaxis: {
          type: 'datetime'
        },
        colors: ['#213af2', '#ff7400', '#30d51d', '#e10707'],
        series: []
      },
      totales: [],
      chartOptions: {
        title: {
          text: 'Totales'
        },
        labels: ['Casos activos', 'Recuperados', 'Muertes'],
        colors: ['#ff7400', '#30d51d', '#e10707'],
        responsive: [{
          breakpoint: 480,
          options: {
            chart: {
              width: 200
            },
            legend: {
              position: 'bottom'
            }
          }
        }]
      }
    }
  },
  mounted () {
    this.$axios.get('getChartLinesData')
      .then(response => {
        this.options.series.push({
          name: 'Confirmados',
          data: response.data.lineOfConfirm
        })
        this.options.series.push({
          name: 'Casos activos',
          data: response.data.lineOfActive
        })
        this.options.series.push({
          name: 'Recuperados',
          data: response.data.lineOfRecovered
        })
        this.options.series.push({
          name: 'Muertes',
          data: response.data.lineOfDeaths
        })
      })
    this.$axios.get('getLastData')
      .then(response => {
        this.totales.push(
          response.data.active,
          response.data.recovered,
          response.data.deaths)
      })
  }
}
</script>
