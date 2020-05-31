<template>
  <q-page class="flex flex-center">
      <apexchart width="1000" type="line" :options="options" :series="series"></apexchart>
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
        chart: {
          id: 'vuechart-example'
        },
        xaxis: {
          categories: []
        }
      },
      series: [{
        name: 'series-1',
        data: []
      }]
    }
  },
  mounted () {
    this.$axios.get('getChartLinesData')
      .then(response => {
        this.options.xaxis.categories = response.data.lineOfConfirm
        this.series = [{
          name: 'infectados-por-dia',
          data: response.data.lineOfConfirm
        }]
      })
  }
}
</script>
