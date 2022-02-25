<template>
  <div v-if="graph" class="fill-height pa-4">
    <v-app-bar app>
      <v-btn :to="toDashboard" icon>
        <v-icon>mdi-arrow-left</v-icon>
      </v-btn>
      <v-toolbar-title>{{ label }}</v-toolbar-title>
      <v-spacer />
      <v-btn outlined color="grey" class="mr-2">
        <v-icon left>mdi-clock</v-icon>
        Last 5 minutes
      </v-btn>
    </v-app-bar>
    <graph-card :config="graph" full-height without-title />
    <v-navigation-drawer :value="true" app right disable-resize-watcher>
      <v-card-title> Options </v-card-title>

      <v-expansion-panels accordion 
          tile>
        <v-expansion-panel
          v-for="(option, index) in options"
          :key="index"
        >
          <v-expansion-panel-header>
            {{ option.category }}
          </v-expansion-panel-header>
          <v-expansion-panel-content>
            <v-select
              v-for="(setting, jndex) in option.settings"
              :key="jndex"
              :label="setting.name"
            />
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-expansion-panels>
    </v-navigation-drawer>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import { Dashboard, Graph } from '~/models'
export default Vue.extend({
  props: {
    dashboard: {
      type: Object as Vue.PropType<Dashboard>,
      required: true,
    },
  },
  data: () => ({
    graph: null as Graph | null,
    options: [
      {
        category: 'Data',
        settings: [
          {
            name: 'Key',
          },
          {
            name: 'Time',
          },
        ],
      },
      {
        category: 'Graph',
        settings: [
          {
            name: 'Color',
          },
          {
            name: 'Unit',
          },
          {
            name: 'Style',
          },
        ],
      },
    ],
  }),
  async fetch() {
    this.graph = await this.$axios.$get(`/graphs/${this.id}`)
  },
  computed: {
    id(): string {
      return this.$route.params.id
    },
    label(): string {
      const { graph } = this

      if (!graph) {
        return 'loading...'
      }

      return graph.label
    },
    toDashboard(): string {
      return `/dashboards/${this.graph?.dashboardId}`
    },
  },
})
</script>
