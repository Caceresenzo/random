declare module 'vue-masonry' {
  import { PluginFunction } from 'vue'

  export class VueMasonryPlugin {
    static install: PluginFunction<never>
  }
}

declare module 'vue/types/vue' {
  interface Vue {
    $redrawVueMasonry(id: string): void
    $redrawVueMasonry(): void
  }
}
