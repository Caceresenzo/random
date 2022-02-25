<template>
  <div>
    <draggable v-model="graphs" class="row" @end="save">
      <v-col
        v-for="graph in graphs"
        :key="graph.id"
        cols="12"
        :sm="graph.width"
      >
        <graph-card :config="graph" @resize="resizingGraph = $event" />
      </v-col>
    </draggable>
    <v-navigation-drawer v-model="resizer" app right disable-resize-watcher>
      <v-card-title>
        Resize card
        <v-spacer />
        <v-btn icon small @click="resizer = false">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>
      <v-card-subtitle v-if="resizingGraph">
        {{ resizingGraph.label }}
      </v-card-subtitle>
      <v-select label="Width" />
      <v-select label="Height" />
    </v-navigation-drawer>
  </div>
</template>

<script lang="ts">
import Vue from 'vue'
import draggable from 'vuedraggable'
import { Dashboard, Graph } from '~/models'
export default Vue.extend({
  components: {
    draggable,
  },
  props: {
    dashboard: {
      type: Object as Vue.PropType<Dashboard>,
      required: true,
    },
  },
  data: () => ({
    graphs: [] as Array<Graph>,
    resizingGraph: null as Graph | null,
  }),
  async fetch() {
    const graphs = await this.$axios.$get(`/dashboards/${this.id}/graphs`)

    Object.assign(this, {
      graphs,
    })

    this.$nextTick(this.$redrawVueMasonry)
  },
  computed: {
    id(): string {
      return this.$route.params.id
    },
    resizer: {
      get(): boolean {
        return !!this.resizingGraph
      },
      set(val: boolean) {
        if (!val) {
          this.resizingGraph = null
        }
      },
    },
  },
  methods: {
    save() {
      this.graphs.forEach((x, index) => {
        console.log(`index: ${index}  => ${x.id}`)
      })
    },
  },
})
</script>
