<template>
  <div class="pa-4 fill-height">
    <v-navigation-drawer app>
      <v-card-title>ATOMOS System</v-card-title>
      <v-card-subtitle>v0.5</v-card-subtitle>
      <v-list>
        <v-subheader>
          Device
          <v-spacer />
          <v-btn small icon>
            <v-icon>mdi-plus</v-icon>
          </v-btn>
        </v-subheader>
        <v-alert type="info" dense class="mx-2">
          No device found
        </v-alert>
        <v-subheader>
          Dashboard
          <v-spacer />
          <v-btn small icon>
            <v-icon>mdi-plus</v-icon>
          </v-btn>
        </v-subheader>
        <v-list-item
          v-for="dashboard in dashboards"
          :key="dashboard.id"
          :to="to(dashboard)"
        >
          <v-list-item-icon>
            <v-icon>mdi-view-dashboard</v-icon>
          </v-list-item-icon>
          <v-list-item-title>{{ dashboard.name }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>

    <nuxt-child :key="$route.params.id" />
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import { Dashboard } from '~/models'
export default Vue.extend({
  data: () => ({
    dashboards: [] as Array<Dashboard>,
  }),
  async fetch() {
    this.dashboards = await this.$axios.$get(`/dashboards`)
  },
  methods: {
    to(dashboard: Dashboard) {
      return `/dashboards/${dashboard.id}`
    },
  },
})
</script>
