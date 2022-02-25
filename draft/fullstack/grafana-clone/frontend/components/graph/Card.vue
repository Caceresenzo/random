<template>
  <v-card :class="cardClasses" :loading="$fetchState.pending">
    <v-hover v-if="!withoutTitle">
      <template #default="{ hover }">
        <v-card-title>
          {{ config.label }}
          <template v-if="hover">
            <v-spacer />
            <v-btn icon small class="mr-2" @click="openResize">
              <v-icon>mdi-resize</v-icon>
            </v-btn>
            <v-btn icon small class="mr-2" :to="toGraph">
              <v-icon>mdi-cog</v-icon>
            </v-btn>
            <v-btn icon small :loading="$fetchState.pending" @click="$fetch">
              <v-icon>mdi-refresh</v-icon>
            </v-btn>
          </template>
        </v-card-title>
      </template>
    </v-hover>
    <apexchart
      :style="chartStyles"
      :height="height"
      width="100%"
      :options="chartOptions"
      :series="series"
    />
  </v-card>
</template>

<script lang="ts">
import Vue from 'vue'
import { Graph } from '~/models'
export default Vue.extend({
  props: {
    config: {
      type: Object as Vue.PropType<Graph>,
      required: true,
    },
    fullHeight: {
      type: Boolean,
    },
    withoutTitle: {
      type: Boolean,
    },
  },
  data: () => ({
    data: [] as Array<[number, number]>,
  }),
  async fetch() {
    this.data = await this.$axios.$get(`/query`, {
      params: {
        property: this.config.key,
      },
    })
  },
  computed: {
    cardClasses(): {} {
      return {
        'fill-height': this.fullHeight,
      }
    },
    chartStyles(): {} {
      return {
        'max-height': `${this.height}px !important`,
      }
    },
    height(): number | string {
      if (this.fullHeight) {
        return '100%'
      }

      return Math.max(100, this.config.height * 100)
    },
    series(): Array<any> {
      return [
        {
          name: 'Test',
          data: this.data,
        },
      ]
    },
    chartOptions(): any {
      const { dark } = this.$vuetify.theme
      const mode = dark ? 'dark' : 'light'
      const background = dark ? '#000' : '#fff'

      return {
        theme: {
          mode,
        },
        grid: {},
        chart: {
          background,
          type: 'line',
          height: 50,
          width: 50,
          toolbar: {
            show: false,
          },
          zoom: {
            enabled: false,
          },
        },
        stroke: {
          curve: 'smooth',
          width: 5,
        },
        xaxis: {
          type: 'datetime',
        },
        yaxis: {
          labels: {
            formatter: (value: number | null) => {
              if (typeof value !== 'number') {
                return `? ${this.config.unit}`
              }

              return `${value.toFixed(2)} ${this.config.unit}`
            },
          },
        },
      }
    },
    toGraph(): string {
      return `/graphs/${this.config.id}`
    },
  },
  methods: {
    openResize() {
      this.$emit('resize', this.config)
    },
  },
})
</script>
