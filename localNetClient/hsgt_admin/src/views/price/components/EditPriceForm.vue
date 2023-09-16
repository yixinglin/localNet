<template>
  <div>
    <el-form ref="dataForm" :model="formData" label-position="left" label-width="120px" style="width: 700px; margin-left:50px;">
      <el-form-item label="Product ID" prop="productId">
        <span> {{ formData.id }}</span>
      </el-form-item>
      <el-form-item label="Product" prop="product">
        <span> {{ formData.productName }}</span>
      </el-form-item>
      <el-form-item label="Quantity" prop="quantity">
        <el-input-number v-model="formData.quantity" :min="0"/>
      </el-form-item>
      <el-form-item label="Price" prop="price">
        <el-input-number v-model="formData.price" :min="0.01" :step="0.1"/>
      </el-form-item>
      <!-- <el-form-item label="Shippment" prop="shippingGroup">
        <span> {{ formData.shippingGroup.groupName }}</span>
      </el-form-item> -->
      <el-form-item label="Shipment" prop="shipment">
        <el-select
          v-model="formData.shippingGroup.id"
          placeholder="Please select"
          @click.native="handleShipmentListDownload()"
        >
          <el-option v-for="item in shipmentList" :key="item.id" :label="item.groupName" :value="item.id" />
        </el-select>
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button @click="closeDialog()">
        Cancel
      </el-button>
      <el-button :loading="loading" type="primary" @click="updateData()">
        Confirm
      </el-button>
    </div>
  </div>
</template>

<script>

import { mapState } from 'vuex'
import { makeOfferObject } from '@/api/price-update'

export default {
  name: 'EditPriceForm',
  props: {
    title: {
      type: String,
      default: 'Edit'
    },
    productId: {
      type: String,
      default: 'id'
    }
  },
  computed: {
    ...mapState({
      // list: state => Object.assign({}, state.autoPriceUpdate.list)
      list(state) {
        return state.autoPriceUpdate.list
      }
    })
  },
  data() {
    return {
      loading: false,
      shipmentList: [],
      formData: {
        type: Object,
        default: () => {
          return this.resetForm()
        }
      }
    }
  },
  watch: {
    'productId': {
      immediate: true,
      handler(newId, oldId) {
        var item = this.list.filter(a => a.id === newId)[0]
        this.formData = Object.assign({}, item)
        this.shipmentList = [item.shippingGroup]
      }
    }
  },
  methods: {
    closeDialog() {
      console.log('close')
      this.$emit('closeDialog')
      // this.formData = this.resetForm()
    },
    updateData() {
      console.log('update data')
      // Update to backend
      this.loading = true
      // var copyFom = Object.assign({}, this.formData)
      var offer = makeOfferObject()
      offer.id = this.formData.id
      offer.price = this.formData.price
      offer.quantity = this.formData.quantity
      offer.shippingGroup.id = this.formData.shippingGroup.id
      try {
        this.$store.dispatch('autoPriceUpdate/requestPricing', offer)
      } catch (err) {
        console.error(err)
      }
      this.loading = false
      this.closeDialog()
    },
    async handleShipmentListDownload() {
      console.log('handleShipmentListDownload')
      try {
        this.shipmentList = await this.$store.dispatch('autoPriceUpdate/generateShipmentList')
      } catch (err) {
        console.error(err)
      }
    }
  }
}

</script>

<style scoped>

</style>
