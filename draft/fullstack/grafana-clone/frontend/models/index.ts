export interface Entity {
  id: number
  createdAt: string
  updatedAt: string
}

export interface Dashboard extends Entity {
  name: string
}

export enum GraphType {
  LINE = "line",
  BUBBLE = "bubble",
  MAP = "map",
}

export interface Graph extends Entity {
  key: string
  label: string
  unit: string
  type: GraphType
  width: number
  height: number
  dashboardId: number
}
