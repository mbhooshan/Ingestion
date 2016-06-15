package com.skry.ingestion;

import java.util.concurrent.ExecutionException;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.StoredBlock;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.BlockStoreException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	try {
			WalletAppKit kit = new WalletAppKit(MainNetParams.get(), new java.io.File("."), "test");
			// Download headers
			kit.startAsync();
			BlockChain chain = kit.chain();
			BlockStore bs = chain.getBlockStore();
			Peer peer = kit.peerGroup().getDownloadPeer();
			// Get last block
			StoredBlock current = bs.getChainHead();
			// Loop until you reach the genesis block
			while(current.getHeight() > 1) {
			    // Fully download blocks between 100 and 200
			    if(100 <= current.getHeight() && current.getHeight() <= 200) {
			        Block b = peer.getBlock(current.getHeader().getHash()).get();
			        System.out.println(b);
			    }
			    current = current.getPrev(bs);
			}
		} catch (BlockStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
