<template>
  <q-page class="q-pa-md body--body">
    <div>
      <q-toggle v-model="modeDark" label="Modo oscuro" @input="changeMode" />
    </div>
    <div class="q-pa-md" style="width: 250px">
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
    <div class="row q-pa-md">
        <div class="col">
          <q-field bg-color="info" standout label="Confirmados" stack-label>
            <template v-slot:control>
              <div class="self-center text-weight-bold text-h5 full-width no-outline" tabindex="0">{{formatter(lastData.confirmed)}}</div>
              <div>
                <q-icon name="add_circle_outline" style="font-size: 1.2em;"/>
                {{formatter(lastData.newConfirmed)}}
              </div>
            </template>
          </q-field>
        </div>
        <div class="col">
          <q-field bg-color="orange" standout label="Activos" stack-label>
            <template v-slot:control>
              <div class="self-center text-weight-bold text-h5 full-width no-outline" tabindex="0">{{formatter(lastData.active)}}</div>
              <div>-</div>
            </template>
          </q-field>
        </div>
        <div class="col">
          <q-field  bg-color="green" standout label="Recuperados" stack-label>
            <template v-slot:control>
              <div class="self-center text-weight-bold text-h5 full-width no-outline" tabindex="0">{{formatter(lastData.recovered)}}</div>
              <div>
                <q-icon name="add_circle_outline" style="font-size: 1.2em;"/>
                {{formatter(lastData.newRecovered)}}
              </div>
            </template>
          </q-field>
        </div>
        <div class="col">
          <q-field bg-color="red" standout label="Muertes" stack-label>
            <template v-slot:control>
              <div class="self-center text-weight-bold text-h5 full-width" tabindex="0">{{formatter(lastData.deaths)}}</div>
              <div>
                <q-icon name="add_circle_outline" style="font-size: 1.2em;"/>
                 {{formatter(lastData.newDeaths)}}
              </div>
            </template>
          </q-field>
        </div>
    </div>
    <chartline :key="componentKeyLine" :modeTheme="mode" :seriesChart="series"></chartline>
    <chartpie :key="componentKeyPie" :modeTheme="mode" :totales="totales"></chartpie>
  </q-page>
</template>

<script>

import { Loading, Dark } from 'quasar'
import ChartPie from '../components/ChartPie.vue'
import ChartLine from '../components/ChartLine.vue'

export default {
  name: 'PageIndex',
  data () {
    return {
      modeDark: true,
      mode: 'dark',
      componentKeyPie: 1,
      componentKeyLine: -1,
      lastData: {
        confirmed: 0,
        active: 0,
        recovered: 0,
        deaths: 0,
        newConfirmed: 0,
        newRecovered: 0,
        newDeaths: 0
      },
      countrys: [
        { value: 'argentina', label: 'Argentina' },
        { value: 'brazil', label: 'Brasil' },
        { value: 'chile', label: 'Chile' },
        { value: 'colombia', label: 'Colombia' },
        { value: 'ecuador', label: 'Ecuador' },
        { value: 'paraguay', label: 'Paraguay' },
        { value: 'peru', label: 'Perú' },
        { value: 'uruguay', label: 'Uruguay' },
        { value: 'venezuela', label: 'Venezuela' }
      ],
      country: {
        country: 'argentina'
      },
      series: [],
      totales: [],
      modeTheme: 'dark'
    }
  },
  components: {
    chartpie: ChartPie,
    chartline: ChartLine
  },
  methods: {
    async getChartLinesData () {
      const response = await this.$axios.post('getChartLinesData', { country: this.country.country })
      this.series.push({ name: 'Confirmados', data: response.data.lineOfConfirm })
      this.series.push({ name: 'Casos activos', data: response.data.lineOfActive })
      this.series.push({ name: 'Recuperados', data: response.data.lineOfRecovered })
      this.series.push({ name: 'Muertes', data: response.data.lineOfDeaths })

      this.lastData.newConfirmed = response.data.lineOfConfirm[response.data.lineOfConfirm.length - 1][1] - response.data.lineOfConfirm[response.data.lineOfConfirm.length - 2][1]
      this.lastData.newRecovered = response.data.lineOfRecovered[response.data.lineOfRecovered.length - 1][1] - response.data.lineOfRecovered[response.data.lineOfRecovered.length - 2][1]
      this.lastData.newDeaths = response.data.lineOfDeaths[response.data.lineOfDeaths.length - 1][1] - response.data.lineOfDeaths[response.data.lineOfDeaths.length - 2][1]
    },
    async getLastData () {
      const response = await this.$axios.post('getLastData', { country: this.country.country })
      this.totales.push(response.data.active, response.data.recovered, response.data.deaths)
      this.lastData.confirmed = response.data.confirmed
      this.lastData.active = response.data.active
      this.lastData.recovered = response.data.recovered
      this.lastData.deaths = response.data.deaths
    },
    async getData () {
      Loading.show()
      this.totales = []
      this.series = []
      await this.getChartLinesData()
      await this.getLastData()
      Loading.hide()
    },
    changeMode (newValue) {
      this.mode = newValue ? 'dark' : ''
      this.mode = newValue ? 'dark' : ''
      Dark.set(newValue)
      this.componentKeyPie += 1
      this.componentKeyLine -= 1
    },
    formatter (x) {
      return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, '.')
    }
  },
  mounted () {
    Dark.set(true)
    this.getData()
  }
}
</script>
<style scoped>
.body--body{
  min-width: 540px;
}
</style>
