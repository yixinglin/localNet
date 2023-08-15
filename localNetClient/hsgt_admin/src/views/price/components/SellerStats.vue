<template>
  <div>
    <div>
      <span>{{ title }}</span>
    </div>
    <el-table
      v-if="sellerData.length>0"
      :data="sellerData"
      border
      fit
      highlight-current-row
      style="width: 100%"
      show-overflow-tooltip
      :default-sort="{prop: 'unitPrice', order: 'ascending'}"
    >
      <el-table-column label="Seller" align="center" width="200px">
        <template slot-scope="{row}">
          <span v-if="selfName===row.shopName" style="color:red;">{{ row.shopName }}</span>
          <span v-else>{{ row.shopName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Label" align="center" width="90px">
        <template slot-scope="{row}">
          <span>{{ row.label }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="unitPrice"
        label="U.Price"
        align="center"
        width="100px"
        sortable
        :sort-orders="['ascending', 'descending']"
        :sort-method="sortByUnitPrice"
      >
        <template slot-scope="{row}">
          <span>{{ row.price2.toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="totalPrice"
        label="Total"
        align="center"
        width="100px"
        sortable
        :sort-orders="['ascending', 'descending']"
        :sort-method="sortByTotalPrice"
      >
        <template slot-scope="{row}">
          <span>{{ totalPrice(row).toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Shipment1" align="center" width="90px">
        <template slot-scope="{row}">
          <span>{{ row.shippingGroup.unitCost.toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Shipment2" align="center" width="90px">
        <template slot-scope="{row}">
          <span>{{ row.shippingGroup.extraUnitCost.toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="Qty" align="center" width="80px">
        <template slot-scope="{row}">
          <span>{{ row.quantity }}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>

export default {
  name: 'SellerStats',
  props: {
    title: {
      type: String,
      default: 'Sellers'
    },
    sellerData: {
      type: Array,
      default: () => []
    },
    selfName: {
      type: String,
      default: ''
    }
  },
  methods: {
    totalPrice(row) {
      return row.price2 + row.shippingGroup.unitCost
    },
    sortByUnitPrice(n1, n2) {
      return n1.price2 - n2.price2
    },
    sortByTotalPrice(n1, n2) {
      return n1.price2 - n2.price2 + n1.shippingGroup.unitCost - n2.shippingGroup.unitCost
    }
  }
}
</script>

<style>

</style>
