<template>
  <div>
    <el-form ref="dataForm" :model="formData" label-position="left" label-width="120px" style="width: 700px; margin-left:50px;">
      <!-- <el-form-item label="Type" prop="type">
        <el-select v-model="temp.type" class="filter-item" placeholder="Please select">
          <el-option v-for="item in calendarTypeOptions" :key="item.key" :label="item.display_name" :value="item.key" />
        </el-select>
      </el-form-item> -->
      <el-form-item label="Product ID" prop="productId">
        <!-- <el-input v-model="formData.productId" readonly /> -->
        <span> {{ formData.productId }}</span>
      </el-form-item>
      <el-form-item label="Product" prop="product">
        <!-- <el-input v-model="formData.productName" readonly /> -->
        <span> {{ formData.productName }}</span>
      </el-form-item>
      <el-form-item label="Amount" prop="amount">
        <el-input-number v-model="formData.offerAmount" :min="0"/>
      </el-form-item>
      <el-form-item label="Note" prop="note">
        <el-input v-model="formData.offerNote" />
      </el-form-item>
      <el-form-item label="Max Adjust" prop="maxAdjust">
        <el-input-number v-model="formData.maxAdjust" :precision="2" :step="1" :min="0" />
      </el-form-item>
      <el-form-item label="Reduce" prop="reduce">
        <el-input-number v-model="formData.reduce" :precision="2" :step="0.01" :min="0" />
      </el-form-item>
      <el-form-item label="Lowest Price" prop="lowestPrice">
        <el-input-number v-model="formData.lowestPrice" :precision="2" :step="1" :min="0" />
      </el-form-item>
      <el-form-item label="Enabled" prop="enabled">
        <el-switch v-model="formData.enabled" size="large" />
      </el-form-item>
      <el-form-item label="Status">
        <el-select v-model="formData.strategyId" class="filter-item" placeholder="Please select">
          <el-option v-for="item in strategyOptions" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <!-- <el-form-item label="Status">
        <el-select v-model="temp.status" class="filter-item" placeholder="Please select">
          <el-option v-for="item in statusOptions" :key="item" :label="item" :value="item" />
        </el-select>
      </el-form-item>
      <el-form-item label="Imp">
        <el-rate v-model="temp.importance" :colors="['#99A9BF', '#F7BA2A', '#FF9900']" :max="3" style="margin-top:8px;" />
      </el-form-item>
      <el-form-item label="Remark">
        <el-input v-model="temp.remark" :autosize="{ minRows: 2, maxRows: 4}" type="textarea" placeholder="Please input" />
      </el-form-item> -->
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

export default {
  name: 'ConfigUpdateForm',
  props: {
    title: {
      type: String,
      default: 'Form'
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
      strategyOptions: ['UnitPriceStrategy', 'TotalPriceStrategy'],
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
        var conf = this.list.filter(a => a.id === newId)[0].conf
        this.formData = Object.assign({}, conf)
      }
    }
  },
  methods: {
    async updateData() {
      console.log('update data')
      // Update to backend
      this.loading = true
      var copyFom = Object.assign({}, this.formData)
      try {
        await this.$store.dispatch('autoPriceUpdate/uploadConfiguration', copyFom)
        await this.$store.dispatch('autoPriceUpdate/updateSellersById', copyFom.productId)
        await this.$store.dispatch('autoPriceUpdate/updateSuggestById', copyFom.productId)
      } catch (err) {
        console.error(err)
      }
      this.loading = false
      this.closeDialog()
    },
    closeDialog() {
      console.log('close')
      this.$emit('closeDialog')
      // this.formData = this.resetForm()
    },
    resetForm() {
      return {
        productId: 'id',
        productName: 'Product name',
        maxAdjust: 1.0,
        reduce_: 0.01,
        lowestPrice: 0,
        strategy: '',
        note: 'note',
        amount: 1,
        enabled: false
      }
    }
  }
}
</script>

<style scoped>

</style>

