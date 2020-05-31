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
        title: {
          text: 'Argentina',
          align: 'left'
        },
        chart: {
          id: 'vuechart-example'
        },
        xaxis: {
          type: 'datetime',
          title: 'Cantidaed',
          categories: []
        }
      },
      series: []
    }
  },
  methods: {
    toTimestamp (strDate) {
      console.log(strDate)
      var datum = Date.parse(strDate)
      return datum / 1000
    }
  },
  mounted () {
    this.$axios.get('getChartLinesData')
      .then(response => {
        this.series.push({
          name: 'infectados-por-dia',
          data: response.data.lineOfConfirm.data
        })
        console.log(this.toTimestamp(response.data.lineOfConfirm.dates.pop()))
        this.options.xaxis.categories.push(response.data.lineOfConfirm.dates)
      })
  }
}
</script>
