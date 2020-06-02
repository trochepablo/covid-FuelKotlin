<template>
  <q-page class="flex flex-center">
    <div>
      <div class="q-pa-md items-start row q-gutter-md">
        <q-select
          option-value="value"
          option-label="label"
          emit-value
          map-options
          outlined
          stack-label
          @input="getData"
          v-model="country.country"
          :options="countrys" label="Paises"/>
      </div>
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
import { Loading } from 'quasar'

export default {
  name: 'PageIndex',
  components: {
    apexchart: VueApexCharts
  },
  data () {
    return {
      countrys: [
        { value: 'argentina', label: 'Argentina' },
        { value: 'colombia', label: 'Colombia' },
        { value: 'brazil', label: 'Brasil' }
      ],
      country: {
        country: 'argentina'
      },
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
  methods: {
    async getChartLinesData () {
      const response = await this.$axios.post('getChartLinesData', { country: this.country.country })
      this.options.series.push({ name: 'Confirmados', data: response.data.lineOfConfirm })
      this.options.series.push({ name: 'Casos activos', data: response.data.lineOfActive })
      this.options.series.push({ name: 'Recuperados', data: response.data.lineOfRecovered })
      this.options.series.push({ name: 'Muertes', data: response.data.lineOfDeaths })
    },
    async getLastData () {
      const response = await this.$axios.post('getLastData', { country: this.country.country })
      this.totales.push(response.data.active, response.data.recovered, response.data.deaths)
    },
    async getData () {
      Loading.show()
      this.totales = []
      this.options.series = []
      await this.getChartLinesData()
      await this.getLastData()
      Loading.hide()
    }
  },
  mounted () {
    this.getData()
  }
}
</script>
