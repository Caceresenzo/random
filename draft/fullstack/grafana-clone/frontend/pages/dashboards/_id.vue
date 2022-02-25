<template>
  <div class="fill-height">
    <v-app-bar app>
      <v-toolbar-title>{{ label }}</v-toolbar-title>
      <v-spacer />
      <v-btn outlined color="grey" class="mr-2">
        <v-icon left>mdi-clock</v-icon>
        Last 5 minutes
      </v-btn>
      <v-select
        :items="[
          { text: '5s', value: 5 },
          { text: '10s', value: 10 },
          { text: '30s', value: 30 },
        ]"
        outlined
        dense
        hide-details
        style="max-width: 150px"
      >
        <template #prepend-inner>
          <v-icon>mdi-refresh</v-icon>
        </template>
      </v-select>
    </v-app-bar>
    <nuxt-child v-if="dashboard" :key="id" :dashboard="dashboard" />
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import { Dashboard } from '~/models'
export default Vue.extend({
  data: () => ({
    dashboard: null as Dashboard | null,
  }),
  async fetch() {
    this.dashboard = await this.$axios.$get(`/dashboards/${this.id}`)
  },
  computed: {
    id() {
      return this.$route.params.id
    },
    label(): string {
      const { dashboard } = this

      if (dashboard) {
        return dashboard.name
      }

      return 'loading...'
    },
  },
})
</script>
